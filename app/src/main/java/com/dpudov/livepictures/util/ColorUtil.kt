package com.dpudov.livepictures.util

object ColorUtil {
    @JvmStatic
    fun makeSemiTransparentColor(argb: Int, alphaFactor: Float): Int {
        val alpha = (android.graphics.Color.alpha(argb) * alphaFactor).toInt()

        return android.graphics.Color.argb(
            alpha,
            android.graphics.Color.red(argb),
            android.graphics.Color.green(argb),
            android.graphics.Color.blue(argb)
        )
    }
}