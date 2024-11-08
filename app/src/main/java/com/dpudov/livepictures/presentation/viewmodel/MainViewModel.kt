package com.dpudov.livepictures.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.ui.graphics.Color
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.dpudov.domain.model.Animation
import com.dpudov.domain.model.Circle
import com.dpudov.domain.model.DrawableItem
import com.dpudov.domain.model.Frame
import com.dpudov.domain.model.Instrument
import com.dpudov.domain.model.Rect
import com.dpudov.domain.model.Stroke
import com.dpudov.domain.model.Triangle
import com.dpudov.domain.repository.IAnimationRepository
import com.dpudov.domain.repository.IDrawableItemRepository
import com.dpudov.domain.repository.IFrameRepository
import com.dpudov.domain.usecase.GenerateFramesUsecase
import com.dpudov.exporter.repository.IGifExportRepository
import com.dpudov.livepictures.R
import com.dpudov.livepictures.presentation.model.AnimationState
import com.dpudov.livepictures.presentation.model.ButtonState
import com.dpudov.livepictures.presentation.model.FramePreviewData
import com.dpudov.livepictures.presentation.model.GenerationState
import com.dpudov.livepictures.presentation.model.GifPreparationState
import com.dpudov.livepictures.presentation.model.OnItemDrawn
import com.dpudov.livepictures.presentation.model.OnToolChanged
import com.dpudov.livepictures.presentation.model.ToolForStylus
import com.dpudov.livepictures.util.CanvasUtil
import com.dpudov.livepictures.util.ColorUtil.makeSemiTransparentColor
import com.dpudov.livepictures.util.combineAny
import com.dpudov.livepictures.util.tickerFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
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
    private val app: Application,
    private val animationRepository: IAnimationRepository,
    private val frameRepository: IFrameRepository,
    private val gifRepository: IGifExportRepository,
    private val drawableItemRepository: IDrawableItemRepository,
    private val generateFramesUsecase: GenerateFramesUsecase
//    private val instrumentRepository: IInstrumentRepository
) : AndroidViewModel(app) {
    //    val instruments: StateFlow<List<Instrument>> = instrumentRepository.getAvailableInstruments()
//        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    private val refreshTrigger: MutableSharedFlow<Unit> = MutableSharedFlow()
    private val _animationState: MutableStateFlow<AnimationState> =
        MutableStateFlow(AnimationState.Idle)
    val animationState: StateFlow<AnimationState> = _animationState

    val currentAnimation: StateFlow<Animation?> = animationRepository.getLatestAnimation()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _currentFrame: MutableStateFlow<Frame?> = MutableStateFlow(null)
    val currentFrame: StateFlow<Frame?> =
        combineAny(_currentFrame, animationState, refreshTrigger) { frame, animationState, _ ->
            frame
        }
            .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _framesCache: MutableStateFlow<List<Frame>> = MutableStateFlow(emptyList())

    private val backgroundBitmap =
        AppCompatResources.getDrawable(app.applicationContext, R.drawable.paper_texture)
            ?.toBitmap(width = CanvasUtil.DEFAULT_WIDTH, height = CanvasUtil.DEFAULT_HEIGHT)

    val framePreviews: StateFlow<List<FramePreviewData>> = _framesCache
        .map { frameList ->
            frameList.map { frame ->
                val items = drawableItemRepository.getItemsByFrameId(frame.id)
                val result = CanvasUtil.createFullDrawingBitmap(backgroundBitmap, items)
                FramePreviewData(
                    frame = frame,
                    bitmap = result
                )
            }
        }
        .flowOn(Dispatchers.Default)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private val prevFrame: Flow<Frame?> = currentFrame
        .map { frame ->
            val prevId = frame?.prevId ?: return@map null
            frameRepository.loadById(prevId)
        }

    private val redoStack: MutableStateFlow<List<DrawableItem>> = MutableStateFlow(emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val currentItems: StateFlow<List<DrawableItem>> = currentFrame
        .combineAny(refreshTrigger) { frame, _ -> frame }
        .mapLatest { frame ->
            frame ?: return@mapLatest emptyList()
            val items = drawableItemRepository.getItemsByFrameId(frame.id)
            items
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val previousItems: StateFlow<List<DrawableItem>> = prevFrame
        .combineAny(refreshTrigger) { frame, _ -> frame }
        .mapLatest { frame ->
            frame ?: return@mapLatest emptyList()
            val items = drawableItemRepository.getItemsByFrameId(frame.id).map {
                when (it) {
                    is Circle -> it.copy(
                        color = makeSemiTransparentColor(
                            argb = it.color,
                            alphaFactor = 0.5f
                        )
                    )

                    is Stroke -> it.copy(
                        color = makeSemiTransparentColor(
                            argb = it.color,
                            alphaFactor = 0.5f
                        )
                    )

                    is Rect -> it.copy(
                        color = makeSemiTransparentColor(
                            argb = it.color,
                            alphaFactor = 0.5f
                        )
                    )

                    is Triangle -> it.copy(
                        color = makeSemiTransparentColor(
                            argb = it.color,
                            alphaFactor = 0.5f
                        )
                    )
                }
            }
            items
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

    private val _selectedSize: MutableStateFlow<Float> = MutableStateFlow(Instrument.PENCIL_SIZE)
    val selectedSize: StateFlow<Float> = _selectedSize

    val undoState: StateFlow<ButtonState> = currentItems
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

    private val _gifPreparationState: MutableStateFlow<GifPreparationState> =
        MutableStateFlow(GifPreparationState.Idle)
    val gifPreparationState: StateFlow<GifPreparationState> = _gifPreparationState

    private val _generationState: MutableStateFlow<GenerationState> =
        MutableStateFlow(GenerationState.Idle)
    val generationState: StateFlow<GenerationState> = _generationState

    init {
        setupAnimation()
    }

    val onItemDrawn: OnItemDrawn = OnItemDrawn { item ->
        addDrawableItem(item)
    }

    val onToolChanged: OnToolChanged = OnToolChanged { newTool ->
        when (newTool) {
            ToolForStylus.ERASER -> selectInstrument(Instrument.Eraser)
            ToolForStylus.DEFAULT -> selectInstrument(previousInstrument.value)
        }
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

    private fun addDrawableItem(item: DrawableItem) {
        viewModelScope.launch {
            drawableItemRepository.addItem(item)
            redoStack.update { emptyList() }
            refreshTrigger.emit(Unit)
        }
    }

    fun updatePreviewCache() {
        viewModelScope.launch {
            val currentFrame = currentFrame.value ?: return@launch

            val nextFrames = getNextFrames(currentFrame, PAGE_SIZE)
            val prevFrames = getPrevFrames(currentFrame, PAGE_SIZE)

            val cache = prevFrames + currentFrame + nextFrames

            _framesCache.update { cache }
        }
    }

    fun loadNextFrames() {
        viewModelScope.launch {
            val framesInCache = _framesCache.value
            val lastFrame = framesInCache.lastOrNull() ?: return@launch
            val nextFrames = getNextFrames(lastFrame, PAGE_SIZE)

            val newCache = framesInCache.takeLast(PAGE_SIZE) + nextFrames
            _framesCache.update { newCache }
        }
    }

    fun loadPreviousFrames() {
        viewModelScope.launch {
            val framesInCache = _framesCache.value

            val firstFrameId = framesInCache.firstOrNull() ?: return@launch
            val prevFrames = getPrevFrames(firstFrameId, PAGE_SIZE)
            val newCache = prevFrames + framesInCache.take(PAGE_SIZE)
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
            val lastItem = currentItems.value.lastOrNull() ?: return@launch
            redoStack.update {
                it + lastItem
            }
            drawableItemRepository.remove(lastItem)
            refreshTrigger.emit(Unit)
        }
    }

    fun redo() {
        viewModelScope.launch {
            val currentStack = redoStack.value
            if (currentStack.isNotEmpty()) {
                val redoItem = currentStack.last()
                redoStack.update { it.dropLast(1) }
                drawableItemRepository.addItem(redoItem)
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
            val drawableItems = currentItems.value

            val copyFrameId = UUID.randomUUID()
            val copyFrame = currentFrame.copy(
                id = copyFrameId,
                prevId = currentFrame.id,
                nextId = currentFrame.nextId
            )
            val copyStrokes = drawableItems
                .filterIsInstance<Stroke>()
                .map { stroke ->
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
            val copyCircles = drawableItems
                .filterIsInstance<Circle>()
                .map { circle ->
                    val id = UUID.randomUUID()
                    circle.copy(
                        id = id,
                        frameId = copyFrameId
                    )
                }
            frameRepository.addFrame(copyFrame)
            drawableItemRepository.addAll(copyStrokes)
            drawableItemRepository.addAll(copyCircles)
            updateCurrentFrame(copyFrame)
        }
    }

    fun shareAnimation(context: Context) {
        viewModelScope.launch {
            runCatching {
                _gifPreparationState.value = GifPreparationState.Loading
                val currentAnimation = currentAnimation.value ?: return@launch
                val gifDir = File(context.cacheDir, "shared_gifs")
                if (!gifDir.exists()) gifDir.mkdirs()
                val outputFile = File(gifDir, "output.gif")
                withContext(Dispatchers.Default) {
                    gifRepository.start(currentAnimation.fps, outputFile)
                    val firstFrame = frameRepository.loadFirstFrame(currentAnimation.id)
                    var currentFrame = firstFrame
                    while (currentFrame != null) {
                        val items = drawableItemRepository.getItemsByFrameId(currentFrame.id)
                        val fullBitmap = CanvasUtil.createFullDrawingBitmap(backgroundBitmap, items)
                        val scaledForGif = CanvasUtil.createBitmapForGif(fullBitmap)
                        gifRepository.addImages(listOf(scaledForGif), outputFile)
                        val nextId = currentFrame.nextId
                        currentFrame = if (nextId != null) {
                            frameRepository.loadById(nextId)
                        } else {
                            null
                        }
                    }
                    gifRepository.finish(outputFile)
                }

                val uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    outputFile
                )
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/octet-stream"
                    putExtra(Intent.EXTRA_STREAM, uri)
                }
                val title = context.getString(R.string.share_animation_as_gif)
                context.startActivity(Intent.createChooser(intent, title))
            }
                .onSuccess {
                    _gifPreparationState.value = GifPreparationState.Idle
                }
                .onFailure {
                    _gifPreparationState.value = GifPreparationState.Idle
                }
        }
    }

    fun selectFps(fps: Int) {
        viewModelScope.launch {
            val currentAnimation = currentAnimation.value ?: return@launch
            animationRepository.updateAnimation(currentAnimation.copy(fps = fps))
        }
    }

    fun selectSize(size: Float) {
        _selectedSize.update { size }
    }

    fun generateFrames(count: Int) {
        viewModelScope.launch {
            runCatching {
                val animationId = currentAnimation.value?.id ?: return@launch
                if (count > 0) {
                    _generationState.update { GenerationState.Generating(0, count) }
                    generateFramesUsecase(
                        animationId = animationId,
                        count = count,
                        onNumberChanged = { number ->
                            _generationState.update { GenerationState.Generating(number, count) }
                        }
                    )
                }
                val lastFrame = frameRepository.loadLastFrame(animationId) ?: return@launch
                updateCurrentFrame(lastFrame)
            }
                .onSuccess {
                    Log.d(javaClass.simpleName, "Frame generation finished")
                    _generationState.update { GenerationState.Idle }
                }
                .onFailure { throwable ->
                    Log.e(javaClass.simpleName, "Frame generation failed", throwable)
                    _generationState.update { GenerationState.Idle }
                }
        }
    }

    private suspend fun getPrevFrames(startingFrame: Frame, count: Int): List<Frame> {
        val previousFrames = mutableListOf<Frame>()
        var currentFrame: Frame? = startingFrame
        repeat(count) {
            val curPrevId = currentFrame?.prevId
            val prevFrame = if (curPrevId != null) {
                frameRepository.loadPrev(curPrevId)
            } else {
                null
            }
            if (prevFrame != null) {
                previousFrames.add(prevFrame)
                currentFrame = prevFrame
            } else {
                return previousFrames.reversed()
            }

        }
        return previousFrames.reversed()
    }

    private suspend fun getNextFrames(startingFrame: Frame, count: Int): List<Frame> {
        val nextFrames = mutableListOf<Frame>()
        var currentFrame: Frame? = startingFrame
        repeat(count) {
            val curNextId = currentFrame?.nextId
            val nextFrame = if (curNextId != null) {
                frameRepository.loadNext(curNextId)
            } else {
                null
            }
            if (nextFrame != null) {
                nextFrames.add(nextFrame)
                currentFrame = nextFrame
            } else {
                return nextFrames
            }
        }
        return nextFrames
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