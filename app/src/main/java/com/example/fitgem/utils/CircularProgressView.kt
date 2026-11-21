package com.example.fitgem.utils

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.fitgem.R

class CircularProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var progress: Float = 72f
        set(value) {
            field = value.coerceIn(0f, 100f)
            invalidate()
        }

    var strokeWidthPx: Float = 24f
        set(value) {
            field = value
            invalidate()
        }

    var centerText: String = "72%"
        set(value) {
            field = value
            invalidate()
        }

    var centerSubText: String = "You're doing great!"
        set(value) {
            field = value
            invalidate()
        }

    var overrideGradientEndColor: Int? = null
        set(value) {
            field = value
            invalidate()
        }

    private val trackPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    private val subTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    }

    private val rectF = RectF()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        val diameter = width.coerceAtMost(height) - strokeWidthPx * 2
        if (diameter <= 0) return

        val cx = width / 2f
        val cy = height / 2f

        rectF.set(
            cx - diameter / 2f,
            cy - diameter / 2f,
            cx + diameter / 2f,
            cy + diameter / 2f
        )

        // Track
        trackPaint.color = ContextCompat.getColor(context, R.color.secondary_surface)
        trackPaint.strokeWidth = strokeWidthPx
        canvas.drawOval(rectF, trackPaint)

        // Progress arc with soft gradient
        val startColor = ContextCompat.getColor(context, R.color.primary_accent)
        val endColor = overrideGradientEndColor ?: ContextCompat.getColor(context, R.color.soft_lavender)
        
        val matrix = Matrix()
        matrix.postRotate(-90f, cx, cy)
        val gradient = LinearGradient(
            rectF.left, rectF.top, rectF.right, rectF.bottom,
            startColor, endColor, Shader.TileMode.CLAMP
        )
        progressPaint.shader = gradient
        progressPaint.strokeWidth = strokeWidthPx

        val sweepAngle = (progress / 100f) * 360f
        if (sweepAngle > 0) {
            canvas.drawArc(rectF, -90f, sweepAngle, false, progressPaint)
        }

        // Draw Center Text
        textPaint.color = ContextCompat.getColor(context, R.color.text_primary)
        textPaint.textSize = diameter * 0.22f
        val textYOffset = if (centerSubText.isNotEmpty()) -10f else (textPaint.textSize * 0.35f)
        canvas.drawText(centerText, cx, cy + textYOffset, textPaint)

        // Draw Subtitle
        if (centerSubText.isNotEmpty()) {
            subTextPaint.color = ContextCompat.getColor(context, R.color.text_secondary)
            subTextPaint.textSize = diameter * 0.085f
            canvas.drawText(centerSubText, cx, cy + (textPaint.textSize * 0.45f), subTextPaint)
        }
    }
}
