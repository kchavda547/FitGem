package com.example.fitgem

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.fitgem.api.ApiClient
import com.example.fitgem.api.DashboardResponse
import com.example.fitgem.utils.CircularProgressView
import com.example.fitgem.utils.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    private lateinit var tvGreeting: TextView
    private lateinit var tvProfileLetter: TextView
    private lateinit var btnProfileHeader: FrameLayout
    private lateinit var cpvGoalRing: CircularProgressView
    private lateinit var tvGoalPercentLarge: TextView

    private lateinit var tvWorkoutVal: TextView
    private lateinit var tvCaloriesVal: TextView
    private lateinit var tvStepsVal: TextView
    private lateinit var tvWaterVal: TextView

    private lateinit var btnStartWorkout: AppCompatButton
    private lateinit var btnBookmark: ImageView
    private lateinit var bottomNav: BottomNavigationView

    private var isBookmarked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        sessionManager = SessionManager(this)
        sessionManager.applyThemeMode()

        setContentView(R.layout.activity_dashboard)

        // Find views
        tvGreeting = findViewById(R.id.tvGreeting)
        tvProfileLetter = findViewById(R.id.tvProfileLetter)
        btnProfileHeader = findViewById(R.id.btnProfileHeader)
        cpvGoalRing = findViewById(R.id.cpvGoalRing)
        tvGoalPercentLarge = findViewById(R.id.tvGoalPercentLarge)

        tvWorkoutVal = findViewById(R.id.tvWorkoutVal)
        tvCaloriesVal = findViewById(R.id.tvCaloriesVal)
        tvStepsVal = findViewById(R.id.tvStepsVal)
        tvWaterVal = findViewById(R.id.tvWaterVal)

        btnStartWorkout = findViewById(R.id.btnStartWorkout)
        btnBookmark = findViewById(R.id.btnBookmark)
        bottomNav = findViewById(R.id.bottomNav)

        // Set User Name
        val userName = sessionManager.getUserName()
        tvGreeting.text = "Good Morning, $userName 👋"
        if (userName.isNotEmpty()) {
            tvProfileLetter.text = userName.first().uppercase()
        }

        // Configure Circular Goal Ring
        cpvGoalRing.progress = 72f
        cpvGoalRing.centerText = "72%"
        cpvGoalRing.centerSubText = ""

        // Header Avatar Click
        btnProfileHeader.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        // Bookmark Click
        btnBookmark.setOnClickListener {
            isBookmarked = !isBookmarked
            btnBookmark.setImageResource(
                if (isBookmarked) android.R.drawable.btn_star_big_on else android.R.drawable.btn_star_big_off
            )
            val msg = if (isBookmarked) "Workout saved to favorites" else "Workout removed from favorites"
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }

        // Start Workout Button
        btnStartWorkout.setOnClickListener {
            val intent = Intent(this, ActiveWorkoutActivity::class.java).apply {
                putExtra("WORKOUT_TITLE", "Full Body Power")
                putExtra("WORKOUT_DURATION", 45)
                putExtra("WORKOUT_CALORIES", 320)
            }
            startActivity(intent)
        }

        // Fetch Dashboard Data from Laravel REST API (with silent fallback)
        fetchDashboardData()

        // Bottom Navigation
        setupBottomNav()
    }

    private fun fetchDashboardData() {
        ApiClient.apiService.getDashboardData(sessionManager.getUserId())
            .enqueue(object : Callback<DashboardResponse> {
                override fun onResponse(
                    call: Call<DashboardResponse>,
                    response: Response<DashboardResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val data = response.body()!!
                        tvGoalPercentLarge.text = "${data.todayGoalPercent.toInt()}%"
                        cpvGoalRing.progress = data.todayGoalPercent
                        cpvGoalRing.centerText = "${data.todayGoalPercent.toInt()}%"
                        tvWorkoutVal.text = "${data.workoutMin} min"
                        tvCaloriesVal.text = "${data.caloriesBurned} kcal"
                        tvStepsVal.text = String.format("%,d", data.steps)
                        tvWaterVal.text = "${data.waterMl / 1000f} L"
                    }
                }

                override fun onFailure(call: Call<DashboardResponse>, t: Throwable) {
                    // Fallback to pre-loaded local values if server offline
                }
            })
    }

    private fun setupBottomNav() {
        bottomNav.selectedItemId = R.id.nav_home

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true
                R.id.nav_workout -> {
                    startActivity(Intent(this, WorkoutActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
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
        bottomNav.selectedItemId = R.id.nav_home
    }
}
