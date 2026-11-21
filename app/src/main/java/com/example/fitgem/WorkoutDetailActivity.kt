package com.example.fitgem

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton

class WorkoutDetailActivity : AppCompatActivity() {

    private lateinit var tvBack: TextView
    private lateinit var btnStartWorkout: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_workout_detail)

        tvBack = findViewById(R.id.tvBack)
        btnStartWorkout = findViewById(R.id.btnStartWorkout)

        // Set title if passed from Intent
        val workoutTitle = intent.getStringExtra("WORKOUT_TITLE") ?: "Full Body Workout"
        tvBack.text = "← $workoutTitle"

        // Handle Back Click
        tvBack.setOnClickListener {
            finish()
        }

        // Handle Start Workout Button
        btnStartWorkout.setOnClickListener {
            Toast.makeText(this, "Workout Started! Let's crush it! 💪🔥", Toast.LENGTH_LONG).show()
        }
    }
}
