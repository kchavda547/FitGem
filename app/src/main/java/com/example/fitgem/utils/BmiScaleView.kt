package com.example.fitgem.utils

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.fitgem.R

class BmiScaleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var bmiValue: Float = 22.9f
        set(value) {
            field = value.coerceIn(10f, 40f)
            invalidate()
        }

    private val paintBar = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintIndicator = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }
    private val paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        textSize = 28f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    }

    private val colors = intArrayOf(
        Color.parseColor("#8CC8D8"), // Underweight - soft cyan
        Color.parseColor("#8EDC7A"), // Normal - soft green
        Color.parseColor("#F2C66D"), // Overweight - soft yellow
        Color.parseColor("#E98A8A")  // Obese - soft red/coral
    )

    private val labels = arrayOf("Under", "Normal", "Over", "Obese")

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        if (width <= 0 || height <= 0) return

        val barHeight = 24f
        val topY = height * 0.45f
        val segmentWidth = width / 4f

        val rectF = RectF()

        // Draw 4 segments with rounded outer ends
        for (i in 0..3) {
            paintBar.color = colors[i]
            val left = i * segmentWidth + 4f
            val right = (i + 1) * segmentWidth - 4f
            rectF.set(left, topY, right, topY + barHeight)
            canvas.drawRoundRect(rectF, 12f, 12f, paintBar)

            // Draw label below segment
            paintText.color = ContextCompat.getColor(context, R.color.text_secondary)
            canvas.drawText(labels[i], (left + right) / 2f, topY + barHeight + 36f, paintText)
        }

        // Calculate indicator X position (BMI min 15 to max 35 mapped to 0 to width)
        val clampedBmi = bmiValue.coerceIn(15f, 35f)
        val normalized = (clampedBmi - 15f) / 20f
        val indicatorX = (normalized * width).coerceIn(16f, width - 16f)

        // Draw Indicator triangle / circle pin above bar
        paintIndicator.color = ContextCompat.getColor(context, R.color.text_primary)
        canvas.drawCircle(indicatorX, topY - 12f, 12f, paintIndicator)

        // Small indicator pin stem
        val path = Path()
        path.moveTo(indicatorX - 10f, topY - 12f)
        path.lineTo(indicatorX + 10f, topY - 12f)
        path.lineTo(indicatorX, topY + 4f)
        path.close()
        canvas.drawPath(path, paintIndicator)
    }
}
