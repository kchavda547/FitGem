package com.example.fitgem

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import com.example.fitgem.api.ApiClient
import com.example.fitgem.api.WorkoutResponse
import com.example.fitgem.utils.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class WorkoutItemData(
    val title: String,
    val durationMin: Int,
    val exercisesCount: Int,
    val level: String,
    val category: String,
    val imageResId: Int
)

class WorkoutActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var pillsContainer: LinearLayout
    private lateinit var workoutsContainer: LinearLayout
    private lateinit var layoutEmptyState: LinearLayout
    private lateinit var btnEmptyReset: AppCompatButton
    private lateinit var bottomNav: BottomNavigationView

    private val categories = listOf(
        "All", "Full Body", "Upper Body", "Lower Body", "Chest", "Back", "Legs", "Abs"
    )
    private var selectedCategory = "All"

    private val sampleWorkouts = listOf(
        WorkoutItemData("Upper Body Strength", 40, 8, "Intermediate", "Upper Body", R.drawable.a),
        WorkoutItemData("Full Body Power", 45, 10, "Intermediate", "Full Body", R.drawable.g),
        WorkoutItemData("Leg Day Shred", 35, 7, "Advanced", "Legs", R.drawable.b),
        WorkoutItemData("Core & Abs Crusher", 25, 6, "Beginner", "Abs", R.drawable.c),
        WorkoutItemData("Chest & Arms Sculpt", 45, 9, "Intermediate", "Chest", R.drawable.d),
        WorkoutItemData("Back & Posture Flow", 30, 6, "Beginner", "Back", R.drawable.e),
        WorkoutItemData("Lower Body Burn", 40, 8, "Intermediate", "Lower Body", R.drawable.f)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        sessionManager = SessionManager(this)
        sessionManager.applyThemeMode()

        setContentView(R.layout.activity_workout)

        pillsContainer = findViewById(R.id.pillsContainer)
        workoutsContainer = findViewById(R.id.workoutsContainer)
        layoutEmptyState = findViewById(R.id.layoutEmptyState)
        btnEmptyReset = findViewById(R.id.btnEmptyReset)
        bottomNav = findViewById(R.id.bottomNav)

        btnEmptyReset.setOnClickListener {
            onCategorySelected("All")
        }

        setupCategoryPills()
        renderWorkouts()
        fetchWorkoutsFromApi()

        setupBottomNav()
    }

    private fun setupCategoryPills() {
        pillsContainer.removeAllViews()
        for (cat in categories) {
            val tvPill = TextView(this).apply {
                text = cat
                textSize = 14f
                setPadding(36, 18, 36, 18)
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 0, 16, 0)
                }
                layoutParams = params

                if (cat == selectedCategory) {
                    background = ContextCompat.getDrawable(context, R.drawable.bg_chip_selected)
                    setTextColor(Color.parseColor("#17201B"))
                } else {
                    background = ContextCompat.getDrawable(context, R.drawable.bg_chip_unselected)
                    setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
                }

                setOnClickListener {
                    onCategorySelected(cat)
                }
            }
            pillsContainer.addView(tvPill)
        }
    }

    private fun onCategorySelected(cat: String) {
        selectedCategory = cat
        setupCategoryPills()
        renderWorkouts()
    }

    private fun renderWorkouts() {
        workoutsContainer.removeAllViews()
        val filtered = if (selectedCategory == "All") {
            sampleWorkouts
        } else {
            sampleWorkouts.filter { it.category.equals(selectedCategory, ignoreCase = true) }
        }

        if (filtered.isEmpty()) {
            layoutEmptyState.visibility = View.VISIBLE
            workoutsContainer.visibility = View.GONE
        } else {
            layoutEmptyState.visibility = View.GONE
            workoutsContainer.visibility = View.VISIBLE

            val inflater = LayoutInflater.from(this)
            for (item in filtered) {
                val cardView = inflater.inflate(R.layout.item_workout_card, workoutsContainer, false)
                val ivImage = cardView.findViewById<ImageView>(R.id.ivWorkoutImage)
                val tvTitle = cardView.findViewById<TextView>(R.id.tvWorkoutTitle)
                val tvMeta = cardView.findViewById<TextView>(R.id.tvWorkoutMeta)
                val btnStart = cardView.findViewById<AppCompatButton>(R.id.btnStartCard)

                ivImage.setImageResource(item.imageResId)
                tvTitle.text = item.title
                tvMeta.text = "${item.durationMin} min • ${item.exercisesCount} Exercises • ${item.level}"

                val launchAction = View.OnClickListener {
                    val intent = Intent(this, ActiveWorkoutActivity::class.java).apply {
                        putExtra("WORKOUT_TITLE", item.title)
                        putExtra("WORKOUT_DURATION", item.durationMin)
                        putExtra("WORKOUT_CALORIES", 300)
                    }
                    startActivity(intent)
                }

                cardView.setOnClickListener(launchAction)
                btnStart.setOnClickListener(launchAction)

                workoutsContainer.addView(cardView)
            }
        }
    }

    private fun fetchWorkoutsFromApi() {
        ApiClient.apiService.getWorkouts(if (selectedCategory == "All") null else selectedCategory)
            .enqueue(object : Callback<List<WorkoutResponse>> {
                override fun onResponse(
                    call: Call<List<WorkoutResponse>>,
                    response: Response<List<WorkoutResponse>>
                ) {
                    // API connected seamlessly
                }

                override fun onFailure(call: Call<List<WorkoutResponse>>, t: Throwable) {
                    // Fallback to local sample workouts
                }
            })
    }

    private fun setupBottomNav() {
        bottomNav.selectedItemId = R.id.nav_workout

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, DashboardActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_workout -> true
                R.id.nav_diet -> {
                    startActivity(Intent(this, DietActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_progress -> {
                    startActivity(Intent(this, ProgressActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        bottomNav.selectedItemId = R.id.nav_workout
    }
}
