package com.dpudov.livepictures.presentation.viewmodel

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dpudov.domain.model.Animation
import com.dpudov.domain.model.Frame
import com.dpudov.domain.model.Instrument
import com.dpudov.domain.model.Stroke
import com.dpudov.domain.repository.IAnimationRepository
import com.dpudov.domain.repository.IFrameRepository
import com.dpudov.domain.repository.IStrokeRepository
import com.dpudov.livepictures.presentation.model.AnimationState
import com.dpudov.livepictures.presentation.model.ButtonState
import com.dpudov.livepictures.presentation.model.OnStrokeDrawn
import com.dpudov.livepictures.presentation.model.OnToolChanged
import com.dpudov.livepictures.presentation.model.ToolForStylus
import com.dpudov.livepictures.util.combineAny
import com.dpudov.livepictures.util.tickerFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val animationRepository: IAnimationRepository,
    private val frameRepository: IFrameRepository,
    private val strokeRepository: IStrokeRepository,
//    private val instrumentRepository: IInstrumentRepository
) : ViewModel() {
    //    val instruments: StateFlow<List<Instrument>> = instrumentRepository.getAvailableInstruments()
//        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    private val refreshTrigger: MutableSharedFlow<Unit> = MutableSharedFlow()
    private val _animationState: MutableStateFlow<AnimationState> =
        MutableStateFlow(AnimationState.Idle)
    val animationState: StateFlow<AnimationState> = _animationState

    val currentAnimation: StateFlow<Animation?> = animationRepository.getLatestAnimation()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _framesCache: MutableStateFlow<List<Frame>> = MutableStateFlow(emptyList())
    val framesCache: StateFlow<List<Frame>> = _framesCache

    private val _currentFrame: MutableStateFlow<Frame?> = MutableStateFlow(null)
    val currentFrame: StateFlow<Frame?> =
        combineAny(_currentFrame, animationState, refreshTrigger) { frame, animationState, _ ->
            frame
        }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val prevFrame: StateFlow<Frame?> = currentFrame
        .map { frame ->
            val prevId = frame?.prevId ?: return@map null
            frameRepository.loadById(prevId)
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)


    private val redoStack: MutableStateFlow<List<Stroke>> = MutableStateFlow(emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentStrokes: StateFlow<List<Stroke>> = currentFrame
        .combineAny(refreshTrigger) { frame, _ -> frame }
        .mapLatest { frame ->
            frame ?: return@mapLatest emptyList()
            strokeRepository.getStrokesByFrameId(frame.id)
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val previousStrokes: StateFlow<List<Stroke>> = prevFrame
        .combineAny(refreshTrigger) { frame, _ -> frame }
        .mapLatest { frame ->
            frame ?: return@mapLatest emptyList()
            strokeRepository.getStrokesByFrameId(frame.id).map {
                it.copy(color = makeSemiTransparentColor(argb = it.color, alphaFactor = 0.5f))
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _previousInstrument: MutableStateFlow<Instrument> =
        MutableStateFlow(Instrument.Pencil)
    val previousInstrument: StateFlow<Instrument> = _previousInstrument

    private val _selectedInstrument: MutableStateFlow<Instrument> =
        MutableStateFlow(Instrument.Pencil)
    val selectedInstrument: StateFlow<Instrument> = _selectedInstrument

    private val _selectedColor: MutableStateFlow<ULong> = MutableStateFlow(Color.White.value)
    val selectedColor: StateFlow<ULong> = _selectedColor

    val undoState: StateFlow<ButtonState> = currentStrokes
        .map {
            if (it.isEmpty()) ButtonState.Inactive
            else ButtonState.Active
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, ButtonState.Inactive)
    val redoState: StateFlow<ButtonState> = redoStack
        .map { stack ->
            if (stack.isEmpty()) ButtonState.Inactive
            else ButtonState.Active
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, ButtonState.Inactive)
    val pauseState: StateFlow<ButtonState> = animationState
        .map { state ->
            if (state == AnimationState.Running) {
                ButtonState.Active
            } else {
                ButtonState.Inactive
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, ButtonState.Inactive)
    val startState: StateFlow<ButtonState> = animationState
        .map { state ->
            if (state == AnimationState.Running) {
                ButtonState.Inactive
            } else {
                ButtonState.Active
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, ButtonState.Inactive)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val animationTicker: SharedFlow<Unit> = animationState
        .flatMapLatest { state ->
            when (state) {
                AnimationState.Idle -> emptyFlow()
                AnimationState.Running -> tickerFlow(1000L)
            }
        }
        .onEach {
            changeFrame()
        }
        .shareIn(viewModelScope, SharingStarted.Eagerly, replay = 0)

    init {
        setupAnimation()
    }

    val onStrokeDrawn: OnStrokeDrawn = OnStrokeDrawn { stroke ->
        addStroke(stroke)
    }

    val onToolChanged: OnToolChanged = OnToolChanged { newTool ->
        when (newTool) {
            ToolForStylus.ERASER -> selectInstrument(Instrument.Eraser)
            ToolForStylus.DEFAULT -> selectInstrument(previousInstrument.value)
        }
    }

//    private fun setupInstruments() {
//        viewModelScope.launch {
//            instrumentRepository.addInstrument(Instrument.Pencil)
//            instrumentRepository.addInstrument()
//        }
//    }

    private fun makeSemiTransparentColor(argb: Int, alphaFactor: Float): Int {
        // Ensure alphaFactor is between 0 (fully transparent) and 1 (fully opaque)
        val alpha = (android.graphics.Color.alpha(argb) * alphaFactor).toInt()

        return android.graphics.Color.argb(
            alpha,
            android.graphics.Color.red(argb),
            android.graphics.Color.green(argb),
            android.graphics.Color.blue(argb)
        )
    }

    private fun changeFrame() {
        viewModelScope.launch {
            val currentAnimation = currentAnimation.value ?: return@launch
            val currentFrame = currentFrame.value ?: return@launch
            val nextId = currentFrame.nextId
            Log.d(javaClass.simpleName, "Changing frame ${currentFrame.id} to frame $nextId")

            if (nextId == null) {
                Log.d(
                    javaClass.simpleName,
                    "Toggling to first frame of animation: ${currentAnimation.id}"
                )
                val firstFrame = frameRepository.loadFirstFrame(animationId = currentAnimation.id)
                    ?: return@launch
                Log.d(javaClass.simpleName, "First frame id is: ${firstFrame.id}")

                updateCurrentFrame(firstFrame)
            } else {
                val newFrame = frameRepository.loadById(
                    animationId = currentAnimation.id,
                    id = nextId
                ) ?: return@launch
                updateCurrentFrame(newFrame)
            }
        }
    }

    private fun setupAnimation() {
        viewModelScope.launch {
            val animation = animationRepository.getLatestAnimation().firstOrNull()
            if (animation == null) {
                val id = UUID.randomUUID()
                val currentTimestamp = System.currentTimeMillis()
                val newAnimation = Animation(
                    id = id,
                    name = DEFAULT_ANIMATION_NAME,
                    createdAt = currentTimestamp
                )
                animationRepository.addAnimation(newAnimation)
                val frameId = UUID.randomUUID()
                val newFrame = Frame(
                    id = frameId,
                    animationId = id,
                    prevId = null,
                    nextId = null
                )
                frameRepository.addFrame(newFrame)
                updateCurrentFrame(newFrame)
            } else {
                val lastFrame = frameRepository.loadLastFrame(animation.id)
                if (lastFrame == null) {
                    val frameId = UUID.randomUUID()
                    val newFrame = Frame(
                        id = frameId,
                        animationId = animation.id,
                        prevId = null,
                        nextId = null
                    )
                    updateCurrentFrame(newFrame)
                } else {
                    updateCurrentFrame(lastFrame)
                }
            }
        }
    }

    private fun addStroke(stroke: Stroke) {
        viewModelScope.launch {
            strokeRepository.addStroke(stroke)
            refreshTrigger.emit(Unit)
        }
    }

    fun showFrames() {
        viewModelScope.launch {
            val currentAnimationId = currentAnimation.value?.id
            val framesInCache = framesCache.value

            requireNotNull(currentAnimationId) { "No animation available yet" }

            if (framesInCache.isEmpty()) {
                val loadedFrames = frameRepository.loadNextFrames(
                    animationId = currentAnimationId,
                    lastFrameId = null,
                    pageSize = PAGE_SIZE
                )
                _framesCache.update { loadedFrames }
            }
        }
    }

    fun loadNextFrames() {
        viewModelScope.launch {
            val currentAnimationId = currentAnimation.value?.id
            val framesInCache = framesCache.value

            requireNotNull(currentAnimationId) { "No animation available yet" }

            val lastFrameId = framesInCache.lastOrNull()?.id
            val loadedFrames = frameRepository.loadNextFrames(
                animationId = currentAnimationId,
                lastFrameId = lastFrameId,
                pageSize = PAGE_SIZE
            )
            _framesCache.update { loadedFrames }
        }
    }

    fun loadPreviousFrames() {
        viewModelScope.launch {
            val currentAnimationId = currentAnimation.value?.id
            val framesInCache = framesCache.value

            requireNotNull(currentAnimationId) { "No animation available yet" }

            val firstFrameId = framesInCache.firstOrNull()?.id
            val loadedFrames = frameRepository.loadPreviousFrames(
                animationId = currentAnimationId,
                firstFrameId = firstFrameId,
                pageSize = PAGE_SIZE
            )
            _framesCache.update { loadedFrames }
        }
    }

    private suspend fun updateCurrentFrame(newFrame: Frame) {
        _currentFrame.update { newFrame }
        refreshTrigger.emit(Unit)
    }

    fun addFrame() {
        viewModelScope.launch {
            val currentAnimationId = currentAnimation.value?.id
            requireNotNull(currentAnimationId) { "No animation available yet" }
            val currentFrame = currentFrame.value
            val newId = UUID.randomUUID()
            val newFrame = Frame(
                id = newId,
                animationId = currentAnimationId,
                prevId = currentFrame?.id,
                nextId = currentFrame?.nextId
            )
            frameRepository.addFrame(newFrame)
            updateCurrentFrame(newFrame)
        }
    }

    fun deleteFrame() {
        viewModelScope.launch {
            val currentAnimationId = currentAnimation.value?.id
            requireNotNull(currentAnimationId) { "No animation available yet" }
            val currentFrame = currentFrame.value
            if (currentFrame != null) {
                frameRepository.removeFrame(frame = currentFrame)
            }
            val newCurrentFrameId = currentFrame?.prevId ?: currentFrame?.nextId
            if (newCurrentFrameId != null) {
                val newCurrentFrame = frameRepository.loadById(
                    animationId = currentAnimationId,
                    id = newCurrentFrameId
                )
                if (newCurrentFrame != null) {
                    updateCurrentFrame(newCurrentFrame)
                }
            } else {
                val newId = UUID.randomUUID()
                val newFrame = Frame(
                    id = newId,
                    animationId = currentAnimationId,
                    prevId = null,
                    nextId = null
                )
                updateCurrentFrame(newFrame)
            }
        }
    }

    fun selectInstrument(instrument: Instrument) {
        _previousInstrument.update { selectedInstrument.value }
        _selectedInstrument.update { instrument }
    }

    fun selectColor(color: ULong) {
        _selectedColor.update { color }
    }

    fun undo() {
        viewModelScope.launch {
            val lastStroke = currentStrokes.value.lastOrNull() ?: return@launch
            redoStack.update {
                it + lastStroke
            }
            strokeRepository.removeStroke(lastStroke.id)
            refreshTrigger.emit(Unit)
        }
    }

    fun redo() {
        viewModelScope.launch {
            val currentStack = redoStack.value
            if (currentStack.isNotEmpty()) {
                val redoStroke = currentStack.last()
                redoStack.update { it.dropLast(1) }
                strokeRepository.addStroke(redoStroke)
                refreshTrigger.emit(Unit)
            }
        }
    }

    fun startAnimation() {
        _animationState.update { AnimationState.Running }
    }

    fun pauseAnimation() {
        _animationState.update { AnimationState.Idle }
    }

    companion object {
        const val PAGE_SIZE = 5
        const val DEFAULT_ANIMATION_NAME = "animation_proof_of_concept"
    }
}