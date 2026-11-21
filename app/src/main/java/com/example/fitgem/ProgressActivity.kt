package com.example.fitgem

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.fitgem.utils.SessionManager
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProgressActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    private lateinit var filterTabsContainer: LinearLayout
    private lateinit var chartWeight: LineChart
    private lateinit var chartCalories: LineChart
    private lateinit var chartWorkout: BarChart
    private lateinit var bottomNav: BottomNavigationView

    private val filterPeriods = listOf("7D", "30D", "3M", "6M", "1Y")
    private var selectedPeriod = "7D"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(this)
        sessionManager.applyThemeMode()

        setContentView(R.layout.activity_progress)

        filterTabsContainer = findViewById(R.id.filterTabsContainer)
        chartWeight = findViewById(R.id.chartWeight)
        chartCalories = findViewById(R.id.chartCalories)
        chartWorkout = findViewById(R.id.chartWorkout)
        bottomNav = findViewById(R.id.bottomNav)

        setupFilterTabs()
        setupCharts()

        setupBottomNav()
    }

    private fun setupFilterTabs() {
        filterTabsContainer.removeAllViews()
        for (period in filterPeriods) {
            val tvTab = TextView(this).apply {
                text = period
                textSize = 13f
                setPadding(32, 16, 32, 16)
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(0, 0, 12, 0) }
                layoutParams = params

                if (period == selectedPeriod) {
                    background = ContextCompat.getDrawable(context, R.drawable.bg_chip_selected)
                    setTextColor(Color.parseColor("#17201B"))
                } else {
                    background = ContextCompat.getDrawable(context, R.drawable.bg_chip_unselected)
                    setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
                }

                setOnClickListener {
                    selectedPeriod = period
                    setupFilterTabs()
                    setupCharts()
                }
            }
            filterTabsContainer.addView(tvTab)
        }
    }

    private fun setupCharts() {
        val chartTextColor = ContextCompat.getColor(this, R.color.text_secondary)
        val chartGridColor = ContextCompat.getColor(this, R.color.card_stroke)
        val accentColor = ContextCompat.getColor(this, R.color.primary_accent)
        val warningColor = ContextCompat.getColor(this, R.color.color_warning)

        // 1. Weight Line Chart
        val weightEntries = listOf(
            Entry(0f, 74.5f), Entry(1f, 74.0f), Entry(2f, 73.6f),
            Entry(3f, 73.2f), Entry(4f, 72.8f), Entry(5f, 72.4f), Entry(6f, 72.0f)
        )
        val weightDataSet = LineDataSet(weightEntries, "Weight (kg)").apply {
            color = accentColor
            setCircleColor(accentColor)
            lineWidth = 3f
            circleRadius = 5f
            setDrawCircleHole(false)
            setDrawValues(false)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawFilled(true)
            fillColor = accentColor
            fillAlpha = 40
        }
        chartWeight.apply {
            data = LineData(weightDataSet)
            description.isEnabled = false
            legend.isEnabled = false
            setTouchEnabled(true)
            xAxis.apply {
                setTextColor(chartTextColor)
                setDrawGridLines(false)
                valueFormatter = IndexAxisValueFormatter(arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"))
            }
            axisLeft.setTextColor(chartTextColor)
            axisLeft.gridColor = chartGridColor
            axisRight.isEnabled = false
            animateX(800)
            invalidate()
        }

        // 2. Calories Line Chart
        val calEntries = listOf(
            Entry(0f, 310f), Entry(1f, 420f), Entry(2f, 290f),
            Entry(3f, 380f), Entry(4f, 450f), Entry(5f, 320f), Entry(6f, 360f)
        )
        val calDataSet = LineDataSet(calEntries, "Calories (kcal)").apply {
            color = warningColor
            setCircleColor(warningColor)
            lineWidth = 3f
            circleRadius = 5f
            setDrawCircleHole(false)
            setDrawValues(false)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawFilled(true)
            fillColor = warningColor
            fillAlpha = 35
        }
        chartCalories.apply {
            data = LineData(calDataSet)
            description.isEnabled = false
            legend.isEnabled = false
            xAxis.apply {
                setTextColor(chartTextColor)
                setDrawGridLines(false)
                valueFormatter = IndexAxisValueFormatter(arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"))
            }
            axisLeft.setTextColor(chartTextColor)
            axisLeft.gridColor = chartGridColor
            axisRight.isEnabled = false
            animateX(800)
            invalidate()
        }

        // 3. Workout Bar Chart
        val barEntries = listOf(
            BarEntry(0f, 30f), BarEntry(1f, 45f), BarEntry(2f, 25f),
            BarEntry(3f, 40f), BarEntry(4f, 50f), BarEntry(5f, 35f), BarEntry(6f, 45f)
        )
        val barDataSet = BarDataSet(barEntries, "Workout (min)").apply {
            color = accentColor
            setDrawValues(false)
        }
        chartWorkout.apply {
            data = BarData(barDataSet)
            description.isEnabled = false
            legend.isEnabled = false
            xAxis.apply {
                setTextColor(chartTextColor)
                setDrawGridLines(false)
                valueFormatter = IndexAxisValueFormatter(arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"))
            }
            axisLeft.setTextColor(chartTextColor)
            axisLeft.gridColor = chartGridColor
            axisRight.isEnabled = false
            animateY(800)
            invalidate()
        }
    }

    private fun setupBottomNav() {
        bottomNav.selectedItemId = R.id.nav_progress

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
                R.id.nav_diet -> {
                    startActivity(Intent(this, DietActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_progress -> true
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
        bottomNav.selectedItemId = R.id.nav_progress
    }
}
