package com.example.fitgem

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class BodyMeasurementsActivity : AppCompatActivity() {

    private lateinit var tvBack: TextView
    private lateinit var chipWeight: TextView
    private lateinit var chipBodyFat: TextView
    private lateinit var chipMuscleMass: TextView

    private lateinit var tvCurrentWeight: TextView
    private lateinit var tvInitialWeight: TextView
    private lateinit var tvGoalWeight: TextView
    private lateinit var tvMotivation: TextView

    private lateinit var etWeight: EditText
    private lateinit var etChest: EditText
    private lateinit var etWaist: EditText
    private lateinit var etHips: EditText
    private lateinit var etArms: EditText
    private lateinit var etThighs: EditText
    private lateinit var btnSaveMeasurements: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_body_measurements)

        // Find views
        tvBack = findViewById(R.id.tvBack)
        chipWeight = findViewById(R.id.chipWeight)
        chipBodyFat = findViewById(R.id.chipBodyFat)
        chipMuscleMass = findViewById(R.id.chipMuscleMass)

        tvCurrentWeight = findViewById(R.id.tvCurrentWeight)
        tvInitialWeight = findViewById(R.id.tvInitialWeight)
        tvGoalWeight = findViewById(R.id.tvGoalWeight)
        tvMotivation = findViewById(R.id.tvMotivation)

        etWeight = findViewById(R.id.etWeight)
        etChest = findViewById(R.id.etChest)
        etWaist = findViewById(R.id.etWaist)
        etHips = findViewById(R.id.etHips)
        etArms = findViewById(R.id.etArms)
        etThighs = findViewById(R.id.etThighs)
        btnSaveMeasurements = findViewById(R.id.btnSaveMeasurements)

        // Handle Back Click
        tvBack.setOnClickListener {
            finish()
        }

        // Chip selection
        chipWeight.setOnClickListener { selectTab(chipWeight, "Weight") }
        chipBodyFat.setOnClickListener { selectTab(chipBodyFat, "Body Fat") }
        chipMuscleMass.setOnClickListener { selectTab(chipMuscleMass, "Muscle Mass") }

        // Save Measurements
        btnSaveMeasurements.setOnClickListener {
            val weightStr = etWeight.text.toString().trim()
            if (weightStr.isEmpty()) {
                Toast.makeText(this, "Please enter your current weight", Toast.LENGTH_SHORT).show()
                etWeight.requestFocus()
                return@setOnClickListener
            }

            tvCurrentWeight.text = "$weightStr kg"
            Toast.makeText(this, "Body measurements saved successfully! ✨", Toast.LENGTH_SHORT).show()

            // Clear inputs
            etWeight.text.clear()
            etChest.text.clear()
            etWaist.text.clear()
            etHips.text.clear()
            etArms.text.clear()
            etThighs.text.clear()
        }
    }

    private fun selectTab(selectedTab: TextView, tabName: String) {
        val tabs = listOf(chipWeight, chipBodyFat, chipMuscleMass)
        tabs.forEach {
            it.setBackgroundResource(R.drawable.bg_chip_unselected)
        }
        selectedTab.setBackgroundResource(R.drawable.bg_chip_selected)

        when (tabName) {
            "Weight" -> {
                tvCurrentWeight.text = "65 kg"
                tvInitialWeight.text = "70 kg"
                tvGoalWeight.text = "60 kg"
                tvMotivation.text = "✨ You're 50% closer to your weight goal! Keep pushing!"
            }
            "Body Fat" -> {
                tvCurrentWeight.text = "18%"
                tvInitialWeight.text = "24%"
                tvGoalWeight.text = "15%"
                tvMotivation.text = "🔥 Down 6% body fat! Amazing progress!"
            }
            "Muscle Mass" -> {
                tvCurrentWeight.text = "34 kg"
                tvInitialWeight.text = "30 kg"
                tvGoalWeight.text = "36 kg"
                tvMotivation.text = "💪 Gained 4 kg of lean muscle mass! Beast mode!"
            }
        }
    }
}
