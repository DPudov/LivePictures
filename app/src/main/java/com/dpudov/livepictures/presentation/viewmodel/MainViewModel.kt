package com.dpudov.livepictures.presentation.viewmodel

import android.graphics.PointF
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
import com.dpudov.livepictures.presentation.mapper.toData
import com.dpudov.livepictures.presentation.model.OnStrokeDrawn
import com.dpudov.livepictures.presentation.model.OnToolChanged
import com.dpudov.livepictures.presentation.model.Tool
import com.dpudov.livepictures.presentation.model.ToolForStylus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapLatest
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

    val currentAnimation: StateFlow<Animation?> = animationRepository.getLatestAnimation()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _framesCache: MutableStateFlow<List<Frame>> = MutableStateFlow(emptyList())
    val framesCache: StateFlow<List<Frame>> = _framesCache

    private val _currentFrame: MutableStateFlow<Frame?> = MutableStateFlow(null)
    val currentFrame: StateFlow<Frame?> = _currentFrame

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentStrokes: StateFlow<List<Stroke>> = currentFrame
        .mapLatest { frame ->
            frame ?: return@mapLatest emptyList()
            strokeRepository.getStrokesByFrameId(frame.id)
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())


    private val _previousInstrument: MutableStateFlow<Instrument> =
        MutableStateFlow(Instrument.Pencil)
    val previousInstrument: StateFlow<Instrument> = _previousInstrument

    private val _selectedInstrument: MutableStateFlow<Instrument> =
        MutableStateFlow(Instrument.Pencil)
    val selectedInstrument: StateFlow<Instrument> = _selectedInstrument

    private val _selectedColor: MutableStateFlow<ULong> = MutableStateFlow(Color.White.value)
    val selectedColor: StateFlow<ULong> = _selectedColor

    init {
        setupAnimation()
    }

    val onStrokeDrawn: OnStrokeDrawn = OnStrokeDrawn { points, tool, color, strokeWidth ->
        addStroke(points, tool, color, strokeWidth)
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
                _currentFrame.update { newFrame }
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
                    _currentFrame.update { newFrame }
                } else {
                    _currentFrame.update { lastFrame }
                }
            }
        }
    }

    private fun addStroke(points: List<PointF>, tool: Tool, color: Int, strokeWidth: Float) {
        viewModelScope.launch {
            val currentFrameId = currentFrame.value?.id ?: return@launch
            val strokeId = UUID.randomUUID()
            val timestamp = System.currentTimeMillis()
            val stroke = Stroke(
                id = strokeId,
                frameId = currentFrameId,
                color = color,
                thickness = strokeWidth,
                instrument = tool.toData(),
                finishTimestamp = timestamp,
                points = points.map { it.toData(strokeId) }
            )
            strokeRepository.addStroke(stroke)
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
            _currentFrame.update { newFrame }
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
                _currentFrame.update { newCurrentFrame }
            } else {
                val newId = UUID.randomUUID()
                val newFrame = Frame(
                    id = newId,
                    animationId = currentAnimationId,
                    prevId = null,
                    nextId = null
                )
                _currentFrame.update { newFrame }
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

    companion object {
        const val PAGE_SIZE = 5
        const val DEFAULT_ANIMATION_NAME = "animation_proof_of_concept"
    }
}