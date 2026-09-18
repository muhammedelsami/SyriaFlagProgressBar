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
import kotlin.math.min
import kotlin.math.sin

class StarLineProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paintLine1 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.rgb(0, 100, 0)
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private val paintLine2 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private val paintLine3 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private val starPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.RED
        style = Paint.Style.FILL
    }

    private val starPath = Path()

    // View boyutuna göre onSizeChanged içinde hesaplanır
    private var lineLength = 0f
    private var lineSpacing = 0f
    private var starSize = 0f
    private var maxStarOffset = 0f

    // 0..1 arası animasyon ilerlemesi
    private var progress = 0f
    private val animator = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 1000
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.REVERSE
        interpolator = LinearInterpolator()
        addUpdateListener {
            progress = it.animatedValue as Float
            invalidate()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        // Dikey ölçüler view'ın kısa kenarına, çizgi uzunluğu genişliğe oranlanır;
        // böylece view küçülse de tasarım bozulmaz.
        val base = min(w, h).toFloat()
        lineLength = w * 0.7f
        lineSpacing = base * 0.3f
        paintLine1.strokeWidth = base * 0.075f
        paintLine2.strokeWidth = base * 0.15f
        paintLine3.strokeWidth = base * 0.075f
        starSize = base * 0.065f
        // Dıştaki yıldızlar ortadaki yıldıza değene kadar hareket eder (üst üste binmez)
        maxStarOffset = (lineLength / 3f - starSize * 2f).coerceAtLeast(0f)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cx = width / 2f
        val cy = height / 2f
        val offset = progress * maxStarOffset

        // Üst çizgi
        canvas.drawLine(cx - lineLength / 2, cy - lineSpacing, cx + lineLength / 2, cy - lineSpacing, paintLine1)

        // Orta çizgi (yıldızlı)
        canvas.drawLine(cx - lineLength / 2, cy, cx + lineLength / 2, cy, paintLine2)
        drawStar(canvas, cx - lineLength / 3 + offset, cy, starSize)
        drawStar(canvas, cx, cy, starSize)
        drawStar(canvas, cx + lineLength / 3 - offset, cy, starSize)

        // Alt çizgi
        canvas.drawLine(cx - lineLength / 2, cy + lineSpacing, cx + lineLength / 2, cy + lineSpacing, paintLine3)
    }

    private fun drawStar(canvas: Canvas, cx: Float, cy: Float, size: Float) {
        starPath.reset()
        val angle = Math.PI / 5  // 36 derece
        for (i in 0 until 10) {
            val r = if (i % 2 == 0) size else size / 2.5f
            val x = (cx + r * cos(i * angle)).toFloat()
            val y = (cy + r * sin(i * angle)).toFloat()
            if (i == 0) starPath.moveTo(x, y) else starPath.lineTo(x, y)
        }
        starPath.close()
        canvas.drawPath(starPath, starPaint)
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
