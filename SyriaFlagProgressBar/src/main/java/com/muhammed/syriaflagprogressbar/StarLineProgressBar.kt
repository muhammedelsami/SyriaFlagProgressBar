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
import kotlin.math.cos
import kotlin.math.sin

class StarLineProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paintLine1 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.rgb(0, 100, 0)
        style = Paint.Style.STROKE
        strokeWidth = 15f
        strokeCap = Paint.Cap.ROUND
    }

    private val paintLine2 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 30f
        strokeCap = Paint.Cap.ROUND
    }

    private val paintLine3 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 15f
        strokeCap = Paint.Cap.ROUND
    }

    private val starPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    private var offset = 0f
    private val animator = ValueAnimator.ofFloat(0f, 100f).apply {
        duration = 1000
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.REVERSE
        interpolator = LinearInterpolator()
        addUpdateListener {
            offset = it.animatedValue as Float
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cx = width / 2f
        val cy = height / 2f
        val lineLength = width * 0.7f

        // Üst çizgi
        canvas.drawLine(cx - lineLength / 2, cy - 60, cx + lineLength / 2, cy - 60, paintLine1)

        // Orta çizgi (yıldızlı)
        canvas.drawLine(cx - lineLength / 2, cy, cx + lineLength / 2, cy, paintLine2)
        drawStar(canvas, cx - lineLength / 3 + offset, cy, 15f)
        drawStar(canvas, cx, cy, 15f)
        drawStar(canvas, cx + lineLength / 3 - offset, cy, 15f)

        // Alt çizgi
        canvas.drawLine(cx - lineLength / 2, cy + 60, cx + lineLength / 2, cy + 60, paintLine3)
    }

    private fun drawStar(canvas: Canvas, cx: Float, cy: Float, size: Float) {
        val path = Path()
        val angle = Math.PI / 5  // 36 derece
        for (i in 0 until 10) {
            val r = if (i % 2 == 0) size else size / 2.5
            val x = (cx + r.toFloat() * cos(i.toDouble() * angle)).toFloat()
            val y = (cy + r.toFloat() * sin(i.toDouble() * angle)).toFloat()
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
