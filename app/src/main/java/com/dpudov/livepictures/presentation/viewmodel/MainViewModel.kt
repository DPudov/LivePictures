package com.dpudov.livepictures.presentation.viewmodel

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dpudov.domain.model.Animation
import com.dpudov.domain.model.Frame
import com.dpudov.domain.model.Instrument
import com.dpudov.domain.model.Stroke
import com.dpudov.domain.repository.IAnimationRepository
import com.dpudov.domain.repository.IFrameRepository
import com.dpudov.domain.repository.IStrokeRepository
import com.dpudov.exporter.repository.IGifExportRepository
import com.dpudov.livepictures.R
import com.dpudov.livepictures.presentation.model.AnimationState
import com.dpudov.livepictures.presentation.model.ButtonState
import com.dpudov.livepictures.presentation.model.FramePreviewData
import com.dpudov.livepictures.presentation.model.OnStrokeDrawn
import com.dpudov.livepictures.presentation.model.OnToolChanged
import com.dpudov.livepictures.presentation.model.ToolForStylus
import com.dpudov.livepictures.util.combineAny
import com.dpudov.livepictures.util.tickerFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val animationRepository: IAnimationRepository,
    private val frameRepository: IFrameRepository,
    private val strokeRepository: IStrokeRepository,
    private val gifRepository: IGifExportRepository
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
    val framePreviews: StateFlow<List<FramePreviewData>> = _framesCache
        .map { frameList ->
            frameList.map { frame ->
                val strokes = strokeRepository.getStrokesByFrameId(frame.id)
                val bitmap = Bitmap.createBitmap(1080, 1920, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)

                strokes.forEach {
                    canvas.drawStroke(it)
                }
                FramePreviewData(
                    frame = frame,
                    bitmap = bitmap
                )
            }
        }
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

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

    val addState: StateFlow<ButtonState> = animationState
        .map { state ->
            if (state == AnimationState.Running) {
                ButtonState.Inactive
            } else {
                ButtonState.Active
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, ButtonState.Inactive)

    val removeState: StateFlow<ButtonState> = animationState
        .map { state ->
            if (state == AnimationState.Running) {
                ButtonState.Inactive
            } else {
                ButtonState.Active
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, ButtonState.Inactive)

    val copyState: StateFlow<ButtonState> = animationState
        .map { state ->
            if (state == AnimationState.Running) {
                ButtonState.Inactive
            } else {
                ButtonState.Active
            }
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
        .combineAny(currentAnimation) { state, animation ->
            state to animation
        }
        .flatMapLatest { (state, animation) ->
            when (state) {
                AnimationState.Idle -> emptyFlow()
                AnimationState.Running -> {
                    val fps = animation?.fps ?: Animation.DEFAULT_FPS
                    val period = 1000L / fps
                    tickerFlow(period)
                }
                null -> emptyFlow()
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
                    createdAt = currentTimestamp,
                    fps = Animation.DEFAULT_FPS
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
                    frameRepository.addFrame(newFrame)
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

    fun updatePreviewCache() {
        viewModelScope.launch {
            val currentAnimationId = currentAnimation.value?.id ?: return@launch
            val framesInCache = _framesCache.value

            Log.d(javaClass.simpleName, "Frames in cache: $framesInCache")
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
            val currentAnimationId = currentAnimation.value?.id ?: return@launch
            val framesInCache = _framesCache.value

            val lastFrameId = framesInCache.lastOrNull()?.id
            val loadedFrames = frameRepository.loadNextFrames(
                animationId = currentAnimationId,
                lastFrameId = lastFrameId,
                pageSize = PAGE_SIZE
            )
            val newCache = framesInCache.takeLast(PAGE_SIZE) + loadedFrames
            _framesCache.update { newCache }
        }
    }

    fun loadPreviousFrames() {
        viewModelScope.launch {
            val currentAnimationId = currentAnimation.value?.id ?: return@launch
            val framesInCache = _framesCache.value

            val firstFrameId = framesInCache.firstOrNull()?.id
            val loadedFrames = frameRepository.loadPreviousFrames(
                animationId = currentAnimationId,
                firstFrameId = firstFrameId,
                pageSize = PAGE_SIZE
            )
            val newCache = loadedFrames + framesInCache.take(PAGE_SIZE)
            _framesCache.update { newCache }
        }
    }

    fun selectFrame(newFrame: Frame) {
        _currentFrame.update { newFrame }
        refreshTrigger.tryEmit(Unit)
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
            val currentAnimationId = currentAnimation.value?.id ?: return@launch
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
                frameRepository.addFrame(newFrame)
                updateCurrentFrame(newFrame)
            }
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            val currentAnimationId = currentAnimation.value?.id ?: return@launch
            Log.d(javaClass.simpleName, "Performing reset for animation: $currentAnimationId")
            frameRepository.removeByAnimation(currentAnimationId)
            val newId = UUID.randomUUID()
            val newFrame = Frame(
                id = newId,
                animationId = currentAnimationId,
                prevId = null,
                nextId = null
            )
            frameRepository.addFrame(newFrame)
            updateCurrentFrame(newFrame)
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
        viewModelScope.launch {
            val currentAnimation = currentAnimation.value ?: return@launch
            val lastFrame = frameRepository.loadLastFrame(currentAnimation.id) ?: return@launch
            updateCurrentFrame(lastFrame)
        }
    }

    fun copyFrame() {
        viewModelScope.launch {
            val currentFrame = _currentFrame.value ?: return@launch
            val currentStrokes = currentStrokes.value

            val copyFrameId = UUID.randomUUID()
            val copyFrame = currentFrame.copy(
                id = copyFrameId,
                prevId = currentFrame.id,
                nextId = currentFrame.nextId
            )
            val copyStrokes = currentStrokes.map { stroke ->
                val id = UUID.randomUUID()
                stroke.copy(
                    id = id,
                    frameId = copyFrameId,
                    points = stroke.points.map { point ->
                        point.copy(
                            id = 0L,
                            strokeId = id
                        )
                    }
                )
            }
            frameRepository.addFrame(copyFrame)
            strokeRepository.addAll(copyStrokes)
            updateCurrentFrame(copyFrame)
        }
    }

    fun shareAnimation(context: Context) {
        viewModelScope.launch {
            val currentAnimation = currentAnimation.value ?: return@launch
            val gifDir = File(context.cacheDir, "shared_gifs")
            if (!gifDir.exists()) gifDir.mkdirs()
            val outputFile = File(gifDir, "output.gif")
            withContext(Dispatchers.Default) {
                gifRepository.start(outputFile)
                var lastFrameId: UUID? = null
                do {
                    val frames = frameRepository.loadNextFrames(currentAnimation.id, lastFrameId, 1)
                    Log.d(javaClass.simpleName, "Processing frames: $frames")
                    lastFrameId = frames.lastOrNull()?.id
                    val bitmaps = frames.map { frame ->
                        val strokes = strokeRepository.getStrokesByFrameId(frame.id)
                        val bitmap = Bitmap.createBitmap(1080, 1920, Bitmap.Config.ARGB_8888)
                        val canvas = Canvas(bitmap)

                        strokes.forEach {
                            canvas.drawStroke(it)
                        }
                        bitmap
                    }
                    gifRepository.addImages(bitmaps, outputFile)
                } while (frames.isNotEmpty())
                gifRepository.finish(outputFile)
            }

            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                outputFile
            )
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "image/gif"
                putExtra(Intent.EXTRA_STREAM, uri)
            }
            val title = context.getString(R.string.share_animation_as_gif)
            context.startActivity(Intent.createChooser(intent, title))
        }
    }

    fun selectFps(fps: Int) {
        viewModelScope.launch {
            val currentAnimation = currentAnimation.value ?: return@launch
            animationRepository.updateAnimation(currentAnimation.copy(fps = fps))
        }
    }

    private fun Canvas.drawStroke(stroke: Stroke) {
        if (stroke.points.isEmpty()) return
        val initialPoint = stroke.points.first()
        val path = android.graphics.Path().apply {
            moveTo(initialPoint.x, initialPoint.y)
            for (point in stroke.points.drop(1)) {
                lineTo(point.x, point.y)
            }
        }
        val paint = android.graphics.Paint().apply {
            isAntiAlias = true
            this.color =
                if (stroke.instrument == Instrument.Eraser) android.graphics.Color.TRANSPARENT
                else stroke.color
            xfermode =
                if (stroke.instrument == Instrument.Eraser) PorterDuffXfermode(PorterDuff.Mode.CLEAR)
                else null
            strokeWidth = stroke.thickness
            style = android.graphics.Paint.Style.STROKE
            strokeCap = android.graphics.Paint.Cap.ROUND
            clearShadowLayer()
        }

        drawPath(path, paint)
    }

    companion object {
        const val PAGE_SIZE = 5
        const val DEFAULT_ANIMATION_NAME = "animation_proof_of_concept"
    }
}