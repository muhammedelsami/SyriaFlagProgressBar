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
import kotlin.math.min
import kotlin.math.sin

class FlagLineProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paintTop = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.rgb(0, 100, 0)
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private val paintMiddle = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private val paintBottom = Paint(Paint.ANTI_ALIAS_FLAG).apply {
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
    private var maxLineOffset = 0f

    // 0..1 arası animasyon ilerlemesi
    private var progress = 0f
    private val animator = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 1500
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
        lineSpacing = base * 0.32f
        paintTop.strokeWidth = base * 0.14f
        paintMiddle.strokeWidth = base * 0.2f
        paintBottom.strokeWidth = base * 0.14f
        starSize = base * 0.08f
        // Kayan çizgiler (yuvarlak uçları dahil) view sınırları içinde kalır, kırpılmaz
        maxLineOffset = ((w - lineLength) / 2f - paintTop.strokeWidth / 2f).coerceAtLeast(0f)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cx = width / 2f
        val cy = height / 2f
        val topOffset = progress * maxLineOffset
        val bottomOffset = -topOffset

        // Üst çizgi (sağdan sola hareket ediyor)
        canvas.drawLine(cx + topOffset - lineLength / 2, cy - lineSpacing, cx + topOffset + lineLength / 2, cy - lineSpacing, paintTop)

        // Orta çizgi (sabit)
        canvas.drawLine(cx - lineLength / 2, cy, cx + lineLength / 2, cy, paintMiddle)

        // Alt çizgi (soldan sağa hareket ediyor)
        canvas.drawLine(cx + bottomOffset - lineLength / 2, cy + lineSpacing, cx + bottomOffset + lineLength / 2, cy + lineSpacing, paintBottom)

        // Orta çizgiye 3 yıldız ekleme
        drawStar(canvas, cx - lineLength / 4, cy, starSize)
        drawStar(canvas, cx, cy, starSize)
        drawStar(canvas, cx + lineLength / 4, cy, starSize)
    }

    // ⭐ Yıldız çizen fonksiyon ⭐
    private fun drawStar(canvas: Canvas, cx: Float, cy: Float, size: Float) {
        starPath.reset()
        val angle = PI / 5  // 36 derece
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
