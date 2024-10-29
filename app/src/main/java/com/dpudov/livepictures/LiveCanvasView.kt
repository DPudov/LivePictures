package com.dpudov.livepictures

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.view.MotionEvent
import android.view.View


class LiveCanvasView(
    context: Context,
//    attrs: AttributeSet
) : View(context) {
    private val path = Path()
    private val paint = Paint().apply {
        color = Color.CYAN
        strokeWidth = 5f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
            }

            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
                postInvalidate()
            }

            MotionEvent.ACTION_UP -> {

            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(path, paint)
    }
}