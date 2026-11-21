package com.example.fitgem

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.fitgem.api.ApiClient
import com.example.fitgem.api.MealRequest
import com.example.fitgem.api.WaterRequest
import com.example.fitgem.utils.CircularProgressView
import com.example.fitgem.utils.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

data class MealModel(
    val type: String,
    val name: String,
    val calories: Int
)

class DietActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    private lateinit var cpvCalorieRing: CircularProgressView
    private lateinit var tvCalorieSummaryText: TextView
    private lateinit var btnAddMealHeader: AppCompatButton
    private lateinit var mealsContainer: LinearLayout

    private lateinit var cpvWaterRing: CircularProgressView
    private lateinit var tvWaterProgressText: TextView
    private lateinit var btnAddWater250: AppCompatButton
    private lateinit var btnAddWater500: AppCompatButton
    private lateinit var btnAddWater750: AppCompatButton
    private lateinit var tvWaterLogTimeline: TextView

    private lateinit var bottomNav: BottomNavigationView

    private var currentConsumedKcal = 1850
    private val targetKcal = 2200

    private var currentWaterMl = 1500
    private val targetWaterMl = 2000

    private val mealsList = mutableListOf(
        MealModel("Breakfast", "Oatmeal + Fruits", 450),
        MealModel("Lunch", "Rice + Vegetables + Protein", 650),
        MealModel("Snack", "Fruits + Nuts", 250),
        MealModel("Dinner", "Healthy Protein Bowl", 500)
    )

    private val waterLogs = mutableListOf(
        "• 08:30 AM — 500 ml",
        "• 11:15 AM — 500 ml",
        "• 02:45 PM — 500 ml"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(this)
        sessionManager.applyThemeMode()

        setContentView(R.layout.activity_diet)

        cpvCalorieRing = findViewById(R.id.cpvCalorieRing)
        tvCalorieSummaryText = findViewById(R.id.tvCalorieSummaryText)
        btnAddMealHeader = findViewById(R.id.btnAddMealHeader)
        mealsContainer = findViewById(R.id.mealsContainer)

        cpvWaterRing = findViewById(R.id.cpvWaterRing)
        tvWaterProgressText = findViewById(R.id.tvWaterProgressText)
        btnAddWater250 = findViewById(R.id.btnAddWater250)
        btnAddWater500 = findViewById(R.id.btnAddWater500)
        btnAddWater750 = findViewById(R.id.btnAddWater750)
        tvWaterLogTimeline = findViewById(R.id.tvWaterLogTimeline)

        bottomNav = findViewById(R.id.bottomNav)

        btnAddMealHeader.setOnClickListener {
            showAddMealDialog()
        }

        btnAddWater250.setOnClickListener { addWater(250) }
        btnAddWater500.setOnClickListener { addWater(500) }
        btnAddWater750.setOnClickListener { addWater(750) }

        updateCalorieUI()
        renderMeals()
        updateWaterUI()

        setupBottomNav()
    }

    private fun updateCalorieUI() {
        val percent = (currentConsumedKcal.toFloat() / targetKcal.toFloat() * 100f).coerceIn(0f, 100f)
        cpvCalorieRing.progress = percent
        cpvCalorieRing.centerText = "${currentConsumedKcal}"
        cpvCalorieRing.centerSubText = "of $targetKcal kcal"

        tvCalorieSummaryText.text = String.format("%,d / %,d kcal", currentConsumedKcal, targetKcal)
    }

    private fun renderMeals() {
        mealsContainer.removeAllViews()
        val inflater = LayoutInflater.from(this)

        for (meal in mealsList) {
            val cardView = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                background = getDrawable(R.drawable.bg_card_secondary)
                setPadding(32, 24, 32, 24)
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(0, 0, 0, 16) }
                layoutParams = params
            }

            val textLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }

            val tvType = TextView(this).apply {
                text = meal.type
                setTextAppearance(android.R.style.TextAppearance_Small)
                setTextColor(resources.getColor(R.color.text_secondary, null))
            }

            val tvName = TextView(this).apply {
                text = meal.name
                textSize = 16f
                setTypeface(null, android.graphics.Typeface.BOLD)
                setTextColor(resources.getColor(R.color.text_primary, null))
            }

            textLayout.addView(tvType)
            textLayout.addView(tvName)

            val tvKcal = TextView(this).apply {
                text = "${meal.calories} kcal"
                textSize = 15f
                setTypeface(null, android.graphics.Typeface.BOLD)
                setTextColor(resources.getColor(R.color.primary_accent, null))
            }

            cardView.addView(textLayout)
            cardView.addView(tvKcal)

            mealsContainer.addView(cardView)
        }
    }

    private fun showAddMealDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("+ Add Meal")

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 20, 40, 20)
        }

        val etMealType = EditText(this).apply {
            hint = "Meal Type (e.g. Snack, Dinner)"
            background = getDrawable(R.drawable.bg_input_field)
            setPadding(24, 20, 24, 20)
        }
        val etFoodName = EditText(this).apply {
            hint = "Food Name (e.g. Avocado Toast)"
            background = getDrawable(R.drawable.bg_input_field)
            setPadding(24, 20, 24, 20)
            val p = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            p.setMargins(0, 16, 0, 16)
            layoutParams = p
        }
        val etCalories = EditText(this).apply {
            hint = "Calories (kcal)"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
            background = getDrawable(R.drawable.bg_input_field)
            setPadding(24, 20, 24, 20)
        }

        layout.addView(etMealType)
        layout.addView(etFoodName)
        layout.addView(etCalories)
        builder.setView(layout)

        builder.setPositiveButton("Add") { dialog, _ ->
            val type = etMealType.text.toString().ifEmpty { "Snack" }
            val name = etFoodName.text.toString().ifEmpty { "Healthy Snack" }
            val kcal = etCalories.text.toString().toIntOrNull() ?: 200

            mealsList.add(MealModel(type, name, kcal))
            currentConsumedKcal += kcal

            updateCalorieUI()
            renderMeals()

            // Post to API
            val request = MealRequest(
                userId = sessionManager.getUserId(),
                mealType = type,
                foodName = name,
                calories = kcal,
                proteinG = 15f,
                carbsG = 30f,
                fatsG = 8f
            )
            ApiClient.apiService.addMeal(request).enqueue(object : Callback<com.example.fitgem.api.ApiResponse> {
                override fun onResponse(call: Call<com.example.fitgem.api.ApiResponse>, response: Response<com.example.fitgem.api.ApiResponse>) {}
                override fun onFailure(call: Call<com.example.fitgem.api.ApiResponse>, t: Throwable) {}
            })

            Toast.makeText(this, "Meal added successfully!", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    private fun addWater(amountMl: Int) {
        currentWaterMl += amountMl
        val timeStr = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        waterLogs.add(0, "• $timeStr — $amountMl ml")

        updateWaterUI()

        // Sync to API
        val req = WaterRequest(sessionManager.getUserId(), amountMl)
        ApiClient.apiService.addWater(req).enqueue(object : Callback<com.example.fitgem.api.ApiResponse> {
            override fun onResponse(call: Call<com.example.fitgem.api.ApiResponse>, response: Response<com.example.fitgem.api.ApiResponse>) {}
            override fun onFailure(call: Call<com.example.fitgem.api.ApiResponse>, t: Throwable) {}
        })

        Toast.makeText(this, "+$amountMl ml water logged! 💧", Toast.LENGTH_SHORT).show()
    }

    private fun updateWaterUI() {
        val percent = (currentWaterMl.toFloat() / targetWaterMl.toFloat() * 100f).coerceIn(0f, 100f)
        cpvWaterRing.progress = percent
        cpvWaterRing.centerText = String.format("%,d", currentWaterMl)
        cpvWaterRing.centerSubText = "ml"

        tvWaterProgressText.text = String.format("%,d ml / %,d ml", currentWaterMl, targetWaterMl)
        tvWaterLogTimeline.text = waterLogs.take(5).joinToString("\n")
    }

    private fun setupBottomNav() {
        bottomNav.selectedItemId = R.id.nav_diet

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, DashboardActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_workout -> {
                    startActivity(Intent(this, WorkoutActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_diet -> true
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
        bottomNav.selectedItemId = R.id.nav_diet
    }
}
