package com.example.fitgem

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.fitgem.api.ApiClient
import com.example.fitgem.api.BmiRequest
import com.example.fitgem.utils.BmiScaleView
import com.example.fitgem.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BmiActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    private lateinit var btnBack: ImageView
    private lateinit var etHeight: EditText
    private lateinit var etWeight: EditText
    private lateinit var btnCalculateBmi: AppCompatButton

    private lateinit var tvBmiScore: TextView
    private lateinit var tvBmiCategory: TextView
    private lateinit var bmiScaleView: BmiScaleView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(this)
        sessionManager.applyThemeMode()

        setContentView(R.layout.activity_bmi)

        btnBack = findViewById(R.id.btnBack)
        etHeight = findViewById(R.id.etHeight)
        etWeight = findViewById(R.id.etWeight)
        btnCalculateBmi = findViewById(R.id.btnCalculateBmi)

        tvBmiScore = findViewById(R.id.tvBmiScore)
        tvBmiCategory = findViewById(R.id.tvBmiCategory)
        bmiScaleView = findViewById(R.id.bmiScaleView)

        btnBack.setOnClickListener { finish() }

        // Default initial calculation
        etHeight.setText("175")
        etWeight.setText("72")
        calculateBmi()

        btnCalculateBmi.setOnClickListener {
            calculateBmi()
        }
    }

    private fun calculateBmi() {
        val hStr = etHeight.text.toString().trim()
        val wStr = etWeight.text.toString().trim()

        if (hStr.isEmpty() || wStr.isEmpty()) {
            Toast.makeText(this, "Please enter both height and weight", Toast.LENGTH_SHORT).show()
            return
        }

        val heightCm = hStr.toFloatOrNull() ?: 175f
        val weightKg = wStr.toFloatOrNull() ?: 72f

        if (heightCm <= 0 || weightKg <= 0) return

        val heightM = heightCm / 100f
        val bmi = weightKg / (heightM * heightM)

        val category: String = when {
            bmi < 18.5f -> "Underweight"
            bmi in 18.5f..24.99f -> "Normal Weight"
            bmi in 25.0f..29.99f -> "Overweight"
            else -> "Obese"
        }

        tvBmiScore.text = String.format("BMI %.1f", bmi)
        tvBmiCategory.text = category
        bmiScaleView.bmiValue = bmi

        // Save to API
        val req = BmiRequest(
            userId = sessionManager.getUserId(),
            heightCm = heightCm,
            weightKg = weightKg,
            bmiScore = bmi,
            category = category
        )
        ApiClient.apiService.saveBmi(req).enqueue(object : Callback<com.example.fitgem.api.ApiResponse> {
            override fun onResponse(call: Call<com.example.fitgem.api.ApiResponse>, response: Response<com.example.fitgem.api.ApiResponse>) {}
            override fun onFailure(call: Call<com.example.fitgem.api.ApiResponse>, t: Throwable) {}
        })
    }
}
