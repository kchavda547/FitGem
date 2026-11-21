package com.example.fitgem

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.fitgem.api.ApiClient
import com.example.fitgem.api.GoalRequest
import com.example.fitgem.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class GoalOptionModel(
    val title: String,
    val description: String,
    val icon: String,
    val targetDesc: String,
    val progressPercent: Int
)

class GoalsActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    private lateinit var btnBack: ImageView
    private lateinit var tvActiveGoalTitle: TextView
    private lateinit var tvCurrentVal: TextView
    private lateinit var tvTargetVal: TextView
    private lateinit var pbGoalProgress: ProgressBar
    private lateinit var tvProgressPercentText: TextView

    private lateinit var goalsOptionsContainer: LinearLayout

    private var selectedIndex = 0

    private val goalOptions = listOf(
        GoalOptionModel("Lose Weight", "Burn fat and drop 5 kg comfortably", "🔥", "Lose 5 kg", 65),
        GoalOptionModel("Gain Muscle", "Build lean muscular body strength", "💪", "Gain 4 kg Muscle", 40),
        GoalOptionModel("Build Strength", "Increase overall strength and stamina", "🏋️", "Bench 60 kg", 80),
        GoalOptionModel("Improve Fitness", "Boost endurance and daily mobility", "🏃", "Run 5k in 25 min", 50),
        GoalOptionModel("Maintain Weight", "Keep current healthy lifestyle & diet", "⚖️", "Maintain 72 kg", 90)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(this)
        sessionManager.applyThemeMode()

        setContentView(R.layout.activity_goals)

        btnBack = findViewById(R.id.btnBack)
        tvActiveGoalTitle = findViewById(R.id.tvActiveGoalTitle)
        tvCurrentVal = findViewById(R.id.tvCurrentVal)
        tvTargetVal = findViewById(R.id.tvTargetVal)
        pbGoalProgress = findViewById(R.id.pbGoalProgress)
        tvProgressPercentText = findViewById(R.id.tvProgressPercentText)
        goalsOptionsContainer = findViewById(R.id.goalsOptionsContainer)

        btnBack.setOnClickListener { finish() }

        renderGoalOptions()
        selectGoal(0)
    }

    private fun renderGoalOptions() {
        goalsOptionsContainer.removeAllViews()

        for ((index, option) in goalOptions.withIndex()) {
            val isSelected = (index == selectedIndex)

            val card = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                background = ContextCompat.getDrawable(
                    context,
                    if (isSelected) R.drawable.bg_card_secondary else R.drawable.bg_card
                )
                setPadding(32, 28, 32, 28)
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(0, 0, 0, 16) }
                layoutParams = params
                gravity = android.view.Gravity.CENTER_VERTICAL

                if (isSelected) {
                    elevation = 2f
                }
            }

            val tvIcon = TextView(this).apply {
                text = option.icon
                textSize = 28f
                setPadding(0, 0, 24, 0)
            }

            val detailsLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }

            val tvTitle = TextView(this).apply {
                text = option.title
                textSize = 17f
                setTypeface(null, Typeface.BOLD)
                setTextColor(ContextCompat.getColor(context, R.color.text_primary))
            }

            val tvDesc = TextView(this).apply {
                text = option.description
                textSize = 13f
                setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
            }

            detailsLayout.addView(tvTitle)
            detailsLayout.addView(tvDesc)

            val tvRadio = TextView(this).apply {
                text = if (isSelected) "✓" else "○"
                textSize = 20f
                setTypeface(null, Typeface.BOLD)
                setTextColor(
                    ContextCompat.getColor(
                        context,
                        if (isSelected) R.color.primary_accent else R.color.text_secondary
                    )
                )
            }

            card.addView(tvIcon)
            card.addView(detailsLayout)
            card.addView(tvRadio)

            card.setOnClickListener {
                selectGoal(index)
            }

            goalsOptionsContainer.addView(card)
        }
    }

    private fun selectGoal(index: Int) {
        selectedIndex = index
        renderGoalOptions()

        val item = goalOptions[index]
        tvActiveGoalTitle.text = item.targetDesc
        pbGoalProgress.progress = item.progressPercent
        tvProgressPercentText.text = "Progress: ${item.progressPercent}%"

        Toast.makeText(this, "Goal set to: ${item.title}", Toast.LENGTH_SHORT).show()

        // Sync to API
        val req = GoalRequest(
            userId = sessionManager.getUserId(),
            goalType = item.title,
            targetValue = 67f,
            currentValue = 72f
        )
        ApiClient.apiService.saveGoal(req).enqueue(object : Callback<com.example.fitgem.api.ApiResponse> {
            override fun onResponse(call: Call<com.example.fitgem.api.ApiResponse>, response: Response<com.example.fitgem.api.ApiResponse>) {}
            override fun onFailure(call: Call<com.example.fitgem.api.ApiResponse>, t: Throwable) {}
        })
    }
}
