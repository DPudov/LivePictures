package com.dpudov.livepictures.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import com.dpudov.domain.model.Circle
import com.dpudov.domain.model.DrawableItem
import com.dpudov.domain.model.Instrument
import com.dpudov.domain.model.Rect
import com.dpudov.domain.model.Stroke
import com.dpudov.domain.model.Triangle

object CanvasUtil {
    const val DEFAULT_WIDTH = 1080
    const val DEFAULT_HEIGHT = 1920
    private const val GIF_WIDTH = 240
    private const val GIF_HEIGHT = 426

    @JvmStatic
    fun createFullDrawingBitmap(backgroundLayer: Bitmap?, items: List<DrawableItem>): Bitmap {
        val result = Bitmap.createBitmap(DEFAULT_WIDTH, DEFAULT_HEIGHT, Bitmap.Config.ARGB_8888)
        val resultCanvas = Canvas(result)

        if (backgroundLayer != null) {
            resultCanvas.drawBitmap(backgroundLayer, 0f, 0f, null)
        }

        val drawingLayer =
            Bitmap.createBitmap(DEFAULT_WIDTH, DEFAULT_HEIGHT, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(drawingLayer)

        items.forEach {
            canvas.drawItem(it)
        }

        resultCanvas.drawBitmap(drawingLayer, 0f, 0f, null)
        return result
    }

    @JvmStatic
    fun createBitmapForGif(fullBitmap: Bitmap): Bitmap =
        Bitmap.createScaledBitmap(fullBitmap, GIF_WIDTH, GIF_HEIGHT, true)

    fun Canvas.drawItem(item: DrawableItem) {
        when (item) {
            is Circle -> drawCircle(item)
            is Stroke -> drawStroke(item)
            is Rect -> drawRectangle(item)
            is Triangle -> drawTriangle(item)
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

        drawPath(
            /* path = */ path,
            /* paint = */ paint
        )
    }

    private fun Canvas.drawCircle(circle: Circle) {
        val paint = android.graphics.Paint().apply {
            isAntiAlias = true
            this.color = circle.color
            xfermode = null
            strokeWidth = circle.thickness
            style = android.graphics.Paint.Style.STROKE
            strokeCap = android.graphics.Paint.Cap.ROUND
            clearShadowLayer()
        }

        drawCircle(
            /* cx = */ circle.centerX,
            /* cy = */ circle.centerY,
            /* radius = */ circle.radius,
            /* paint = */ paint
        )
    }

    private fun Canvas.drawRectangle(rect: Rect) {
        val paint = android.graphics.Paint().apply {
            isAntiAlias = true
            this.color = rect.color
            xfermode = null
            strokeWidth = rect.thickness
            style = android.graphics.Paint.Style.STROKE
            strokeCap = android.graphics.Paint.Cap.ROUND
            clearShadowLayer()
        }

        drawRect(
            /* left = */ rect.topLeftX,
            /* top = */ rect.topLeftY,
            /* right = */ rect.topLeftX + rect.width,
            /* bottom = */ rect.topLeftY + rect.height,
            /* paint = */ paint
        )
    }

    private fun Canvas.drawTriangle(triangle: Triangle) {
        val paint = android.graphics.Paint().apply {
            isAntiAlias = true
            this.color = triangle.color
            xfermode = null
            strokeWidth = triangle.thickness
            style = android.graphics.Paint.Style.STROKE
            strokeCap = android.graphics.Paint.Cap.ROUND
            clearShadowLayer()
        }
        val path = android.graphics.Path().apply {
            moveTo(triangle.x1, triangle.y1)
            lineTo(triangle.x2, triangle.y2)
            lineTo(triangle.x3, triangle.y3)
            lineTo(triangle.x1, triangle.y1)
        }
        drawPath(
            /* path = */ path,
            /* paint = */ paint
        )
    }
}

