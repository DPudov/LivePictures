package com.dpudov.livepictures.presentation.ui.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import com.dpudov.domain.model.Instrument


class LiveCanvasView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : SurfaceView(context, attrs, defStyleAttr, defStyleRes), SurfaceHolder.Callback {
    private val path = Path()
    private val paint = Paint().apply {
        color = Color.WHITE
        strokeWidth = 5f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private val brushPaint = Paint().apply {
        color = Color.WHITE
        strokeWidth = 15f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }


    private val eraserPaint = Paint().apply {
        color = Color.TRANSPARENT
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 20f
    }

    private lateinit var bitmap: Bitmap
    private lateinit var canvas: Canvas

    private var currentPaint: Paint = paint
    private var currentPath = Path()

//    init {
//        setupBitmap()
//    }

    init {
        holder.addCallback(this)
        setZOrderOnTop(true)
        holder.setFormat(PixelFormat.TRANSPARENT)
    }

    private fun setupBitmap() {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap)
    }

    fun setInstrument(instrument: Instrument) {
        currentPaint = when (instrument) {
            Instrument.Pencil -> paint
            Instrument.Brush -> brushPaint
            Instrument.Eraser -> eraserPaint
        }
    }

    fun setColor(newColor: Int) {
        currentPaint.apply {
            color = newColor
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false
        val x = event.x
        val y = event.y
        val toolType = event.getToolType(0)
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
//                if (toolType == MotionEvent.TOOL_TYPE_ERASER) {
//                    handleEraserStart(x, y)
//                } else {
//                    handleDrawStart(x, y)
//                }
                currentPath.moveTo(x, y)
                true
            }

            MotionEvent.ACTION_MOVE -> {
//                if (toolType == MotionEvent.TOOL_TYPE_ERASER) {
//                    handleEraserMove(x, y)
//                } else {
//                    handleDrawMove(x, y)
//                }
                currentPath.lineTo(x, y)
                drawPath()
                true
            }

            MotionEvent.ACTION_UP -> {
//                if (toolType == MotionEvent.TOOL_TYPE_ERASER) {
//                    handleEraserEnd(x, y)
//                } else {
//                    handleDrawEnd(x, y)
//                }
                currentPath.lineTo(x, y)
                drawPath()
                currentPath.reset()
                true
            }

            else -> false
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
    }

    private fun handleDrawStart(x: Float, y: Float) {
        path.moveTo(x, y)
//        currentStroke.clear()
//        currentStroke.add(PointF(x, y))
    }

    private fun handleDrawMove(x: Float, y: Float) {
        path.lineTo(x, y)
//        currentStroke.add(PointF(x, y))
        postInvalidate()
    }

    private fun handleDrawEnd(x: Float, y: Float) {
//        currentStroke.add(PointF(x, y))
        // Save the current stroke if needed
    }

    private fun handleEraserStart(x: Float, y: Float) {
        eraseAt(x, y)
    }

    private fun handleEraserMove(x: Float, y: Float) {
        eraseAt(x, y)
    }

    private fun handleEraserEnd(x: Float, y: Float) {
        eraseAt(x, y)
    }

    private fun eraseAt(x: Float, y: Float) {
//        canvas.drawCircle(x, y, eraserPaint.strokeWidth / 2, eraserPaint)
//        postInvalidate()
        val canvas = holder.lockCanvas()
        canvas?.drawCircle(x, y, eraserPaint.strokeWidth / 2, eraserPaint)
        holder.unlockCanvasAndPost(canvas)
    }

    private fun drawPath() {
        val canvas = holder.lockCanvas()
        canvas?.let {
            it.drawPath(currentPath, currentPaint)
            holder.unlockCanvasAndPost(it)
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
//        TODO("Not yet implemented")
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
//        TODO("Not yet implemented")
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
//        TODO("Not yet implemented")
    }
}