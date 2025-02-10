package com.muhammed.syriaflagprogressbar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.animation.ValueAnimator
import android.graphics.Path
import android.view.animation.LinearInterpolator
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class CircleFlagProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint1 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.rgb(0, 100, 0)//color = Color.GREEN
        style = Paint.Style.STROKE
        strokeWidth = 15f
    }

    private val paint2 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeWidth = 25f
    }

    private val paint3 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 15f
    }

    private val starPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    private var angle1 = 0f
    private var angle2 = 0f
    private var angle3 = 0f

    private val animator = ValueAnimator.ofFloat(0f, 360f).apply {
        duration = 2000
        repeatCount = ValueAnimator.INFINITE
        interpolator = LinearInterpolator()
        addUpdateListener {
            angle1 = it.animatedValue as Float
            angle2 = -(it.animatedValue as Float) as Float * 1.5f
            angle3 = it.animatedValue as Float * 2f
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cx = width / 2f
        val cy = height / 2f
        val radius = min(cx, cy) * 0.8f

        // Üç halkayı çiz
        canvas.drawArc(cx - radius, cy - radius, cx + radius, cy + radius, angle1, 300f, false, paint1)
        canvas.drawArc(cx - radius * 0.75f, cy - radius * 0.75f, cx + radius * 0.75f, cy + radius * 0.75f, angle2, 300f, false, paint2)
        canvas.drawArc(cx - radius * 0.5f, cy - radius * 0.5f, cx + radius * 0.5f, cy + radius * 0.5f, angle3, 300f, false, paint3)

//        // Ortadaki halkaya üç kırmızı yıldız ekle
        val middleRadius = radius * 0.745f
        drawStar(canvas, cx, cy - middleRadius, 10f)
        drawStar(canvas, cx - middleRadius * cos(Math.PI / 6).toFloat(), cy + middleRadius * sin(Math.PI / 6).toFloat(), 10f)
        drawStar(canvas, cx + middleRadius * cos(Math.PI / 6).toFloat(), cy + middleRadius * sin(Math.PI / 6).toFloat(), 10f)
    }

    // Yıldız çizen fonksiyon
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

