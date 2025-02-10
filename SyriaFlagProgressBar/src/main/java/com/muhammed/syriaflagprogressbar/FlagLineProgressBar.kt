package com.muhammed.syriaflagprogressbar

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class FlagLineProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paintTop = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.rgb(0, 100, 0)
        strokeWidth = 20f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private val paintMiddle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        strokeWidth = 25f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private val paintBottom = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        strokeWidth = 20f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private val starPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    private var topOffset = 0f
    private var bottomOffset = 0f

    private val animator = ValueAnimator.ofFloat(0f, 200f).apply {
        duration = 1500
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.REVERSE
        interpolator = LinearInterpolator()
        addUpdateListener {
            topOffset = it.animatedValue as Float
            bottomOffset = -(it.animatedValue as Float) as Float
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cx = width / 2f
        val cy = height / 2f
        val lineLength = width * 0.8f

        // Üst çizgi (sağdan sola hareket ediyor)
        canvas.drawLine(cx + topOffset - lineLength / 2, cy - 50f, cx + topOffset + lineLength / 2, cy - 50f, paintTop)

        // Orta çizgi (sabit)
        canvas.drawLine(cx - lineLength / 2, cy, cx + lineLength / 2, cy, paintMiddle)

        // Alt çizgi (soldan sağa hareket ediyor)
        canvas.drawLine(cx + bottomOffset - lineLength / 2, cy + 50f, cx + bottomOffset + lineLength / 2, cy + 50f, paintBottom)

        // Orta çizgiye 3 yıldız ekleme
        drawStar(canvas, cx - lineLength / 4, cy, 20f)
        drawStar(canvas, cx, cy, 20f)
        drawStar(canvas, cx + lineLength / 4, cy, 20f)
    }

    // ⭐ Yıldız çizen fonksiyon ⭐
    private fun drawStar(canvas: Canvas, cx: Float, cy: Float, size: Float) {
        val path = Path()
        val angle = PI / 5  // 36 derece
        for (i in 0 until 10) {
            val r = if (i % 2 == 0) size else size / 2.5
            val x = (cx + r.toFloat() * cos(i * angle)).toFloat()
            val y = (cy + r.toFloat() * sin(i * angle)).toFloat()
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close()
        canvas.drawPath(path, starPaint)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animator.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator.cancel()
    }
}
