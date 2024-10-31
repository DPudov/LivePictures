package com.dpudov.livepictures.presentation.ui.view

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.PointF
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import com.dpudov.domain.model.Instrument
import com.dpudov.domain.model.Stroke
import com.dpudov.livepictures.presentation.model.OnStrokeDrawn
import com.dpudov.livepictures.presentation.model.OnToolChanged
import com.dpudov.livepictures.presentation.model.Tool
import com.dpudov.livepictures.presentation.model.ToolForStylus


class LiveCanvasView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : SurfaceView(context, attrs, defStyleAttr, defStyleRes), SurfaceHolder.Callback {
    var onStrokeDrawnListener: OnStrokeDrawn? = null
    var onToolChangedListener: OnToolChanged? = null

    private val pencilPaint = Paint().apply {
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

//    private lateinit var bitmap: Bitmap

    private var currentTool: Tool = Tool.PENCIL
    private var currentPaint: Paint = pencilPaint
    private val currentPath: Path = Path()
    private val pathPoints: MutableList<PointF> = mutableListOf()

    init {
        holder.addCallback(this)
        setZOrderOnTop(true)
        holder.setFormat(PixelFormat.TRANSPARENT)
    }

    fun setInstrument(instrument: Instrument) {
        currentTool = when (instrument) {
            Instrument.Brush -> Tool.BRUSH
            Instrument.Eraser -> Tool.ERASER
            Instrument.Pencil -> Tool.PENCIL
        }
        currentPaint = when (instrument) {
            Instrument.Pencil -> pencilPaint
            Instrument.Brush -> brushPaint
            Instrument.Eraser -> eraserPaint
        }
    }

    fun setColor(newColor: Int) {
        currentPaint.apply {
            color = newColor
        }
    }

    fun drawStrokes(strokes: List<Stroke>) {

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false
        val x = event.x
        val y = event.y
        val toolType = event.getToolType(0)
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                if (toolType == MotionEvent.TOOL_TYPE_ERASER) {
                    onToolChangedListener?.onToolChanged(ToolForStylus.ERASER)
                } else {
                    onToolChangedListener?.onToolChanged(ToolForStylus.DEFAULT)
                }
                currentPath.moveTo(x, y)
                pathPoints += PointF(x, y)
                true
            }

            MotionEvent.ACTION_MOVE -> {
                if (toolType == MotionEvent.TOOL_TYPE_ERASER) {
                    onToolChangedListener?.onToolChanged(ToolForStylus.ERASER)
                } else {
                    onToolChangedListener?.onToolChanged(ToolForStylus.DEFAULT)
                }
                currentPath.lineTo(x, y)
                pathPoints += PointF(x, y)
                drawPath()
                true
            }

            MotionEvent.ACTION_UP -> {
                if (toolType == MotionEvent.TOOL_TYPE_ERASER) {
                    onToolChangedListener?.onToolChanged(ToolForStylus.ERASER)
                } else {
                    onToolChangedListener?.onToolChanged(ToolForStylus.DEFAULT)
                }
                currentPath.lineTo(x, y)
                drawPath()
                onStrokeDrawnListener?.onStrokeDrawn(pathPoints, currentTool, currentPaint.color, currentPaint.strokeWidth)
                currentPath.reset()
                true
            }

            else -> false
        }
    }

//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//        canvas.drawBitmap(bitmap, 0f, 0f, null)
//    }

    private fun drawPath() {
        val canvas = holder.lockCanvas()
        if (canvas != null) {
            canvas.drawPath(currentPath, currentPaint)
            holder.unlockCanvasAndPost(canvas)
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.d(javaClass.simpleName, "Surface was created")
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.d(
            javaClass.simpleName,
            "Surface was changed. Format: $format, width: $width, height: $height"
        )
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d(javaClass.simpleName, "Surface was destroyed")
    }
}