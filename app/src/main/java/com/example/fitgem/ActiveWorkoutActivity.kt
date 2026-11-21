package com.example.fitgem

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.fitgem.api.ApiClient
import com.example.fitgem.api.WorkoutHistoryRequest
import com.example.fitgem.utils.CircularProgressView
import com.example.fitgem.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class ExerciseModel(
    val name: String,
    val category: String,
    val totalSets: Int,
    val weightKg: Int,
    val reps: Int,
    val restSec: Int
)

class ActiveWorkoutActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    private lateinit var btnBack: ImageView
    private lateinit var tvExerciseName: TextView
    private lateinit var tvExerciseCategory: TextView
    private lateinit var tvSetsValue: TextView
    private lateinit var tvWeightValue: TextView
    private lateinit var tvRepsValue: TextView
    private lateinit var timerCircularProgress: CircularProgressView

    private lateinit var btnCompleteSet: AppCompatButton
    private lateinit var btnPause: AppCompatButton
    private lateinit var btnSkip: AppCompatButton
    private lateinit var btnNextExercise: AppCompatButton

    private val exercises = listOf(
        ExerciseModel("Bench Press", "Chest • Intermediate", 4, 40, 12, 45),
        ExerciseModel("Incline Dumbbell Press", "Upper Chest • Intermediate", 3, 24, 10, 45),
        ExerciseModel("Cable Flyes", "Inner Chest • Beginner", 3, 15, 15, 30),
        ExerciseModel("Tricep Dips", "Arms • Bodyweight", 3, 0, 12, 45)
    )

    private var currentExerciseIndex = 0
    private var currentSet = 1

    private var countDownTimer: CountDownTimer? = null
    private var isTimerRunning = false
    private var isPaused = false
    private var secondsRemaining = 45L
    private var totalRestSec = 45L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        sessionManager = SessionManager(this)
        sessionManager.applyThemeMode()

        setContentView(R.layout.activity_active_workout)

        btnBack = findViewById(R.id.btnBack)
        tvExerciseName = findViewById(R.id.tvExerciseName)
        tvExerciseCategory = findViewById(R.id.tvExerciseCategory)
        tvSetsValue = findViewById(R.id.tvSetsValue)
        tvWeightValue = findViewById(R.id.tvWeightValue)
        tvRepsValue = findViewById(R.id.tvRepsValue)
        timerCircularProgress = findViewById(R.id.timerCircularProgress)

        btnCompleteSet = findViewById(R.id.btnCompleteSet)
        btnPause = findViewById(R.id.btnPause)
        btnSkip = findViewById(R.id.btnSkip)
        btnNextExercise = findViewById(R.id.btnNextExercise)

        btnBack.setOnClickListener {
            showExitConfirmation()
        }

        btnCompleteSet.setOnClickListener {
            onCompleteSetClicked()
        }

        btnPause.setOnClickListener {
            togglePauseTimer()
        }

        btnSkip.setOnClickListener {
            cancelTimer()
            nextExercise()
        }

        btnNextExercise.setOnClickListener {
            cancelTimer()
            nextExercise()
        }

        loadExercise(0)
    }

    private fun loadExercise(index: Int) {
        if (index >= exercises.size) {
            showWorkoutCompletedDialog()
            return
        }
        currentExerciseIndex = index
        currentSet = 1

        val ex = exercises[index]
        tvExerciseName.text = ex.name
        tvExerciseCategory.text = ex.category
        tvSetsValue.text = "$currentSet / ${ex.totalSets}"
        tvWeightValue.text = if (ex.weightKg > 0) "${ex.weightKg} kg" else "BW"
        tvRepsValue.text = "${ex.reps}"

        totalRestSec = ex.restSec.toLong()
        secondsRemaining = totalRestSec

        updateTimerDisplay(secondsRemaining, totalRestSec)
    }

    private fun onCompleteSetClicked() {
        val ex = exercises[currentExerciseIndex]
        if (currentSet < ex.totalSets) {
            currentSet++
            tvSetsValue.text = "$currentSet / ${ex.totalSets}"
            Toast.makeText(this, "Set ${currentSet - 1} completed! Rest time started.", Toast.LENGTH_SHORT).show()
            startRestTimer(totalRestSec)
        } else {
            Toast.makeText(this, "${ex.name} completed! Moving to next exercise.", Toast.LENGTH_SHORT).show()
            nextExercise()
        }
    }

    private fun startRestTimer(seconds: Long) {
        cancelTimer()
        isTimerRunning = true
        isPaused = false
        btnPause.text = "Pause"

        countDownTimer = object : CountDownTimer(seconds * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateTimerDisplay(secondsRemaining, totalRestSec)
            }

            override fun onFinish() {
                isTimerRunning = false
                secondsRemaining = 0
                updateTimerDisplay(0, totalRestSec)
                Toast.makeText(this@ActiveWorkoutActivity, "Rest over! Ready for next set 💪", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    private fun togglePauseTimer() {
        if (!isTimerRunning && secondsRemaining <= 0) return

        if (isPaused) {
            isPaused = false
            btnPause.text = "Pause"
            startRestTimer(secondsRemaining)
        } else {
            isPaused = true
            btnPause.text = "Resume"
            countDownTimer?.cancel()
        }
    }

    private fun cancelTimer() {
        countDownTimer?.cancel()
        isTimerRunning = false
        isPaused = false
        btnPause.text = "Pause"
    }

    private fun updateTimerDisplay(remainingSec: Long, totalSec: Long) {
        val min = remainingSec / 60
        val sec = remainingSec % 60
        val timeFormatted = String.format("%02d:%02d", min, sec)

        val progressPercent = if (totalSec > 0) ((remainingSec.toFloat() / totalSec.toFloat()) * 100f) else 0f

        timerCircularProgress.progress = progressPercent
        timerCircularProgress.centerText = timeFormatted
        timerCircularProgress.centerSubText = if (remainingSec > 0) "Resting" else "Ready"
    }

    private fun nextExercise() {
        if (currentExerciseIndex + 1 < exercises.size) {
            loadExercise(currentExerciseIndex + 1)
        } else {
            showWorkoutCompletedDialog()
        }
    }

    private fun showWorkoutCompletedDialog() {
        cancelTimer()
        
        // Log workout history to Laravel REST API
        val workoutTitle = intent.getStringExtra("WORKOUT_TITLE") ?: "Full Body Power"
        val duration = intent.getIntExtra("WORKOUT_DURATION", 45)
        val calories = intent.getIntExtra("WORKOUT_CALORIES", 320)

        val request = WorkoutHistoryRequest(
            userId = sessionManager.getUserId(),
            workoutTitle = workoutTitle,
            durationMinutes = duration,
            caloriesBurned = calories,
            completedAt = System.currentTimeMillis().toString()
        )
        ApiClient.apiService.logWorkout(request).enqueue(object : Callback<com.example.fitgem.api.ApiResponse> {
            override fun onResponse(call: Call<com.example.fitgem.api.ApiResponse>, response: Response<com.example.fitgem.api.ApiResponse>) {}
            override fun onFailure(call: Call<com.example.fitgem.api.ApiResponse>, t: Throwable) {}
        })

        AlertDialog.Builder(this)
            .setTitle("🎉 Workout Completed!")
            .setMessage("Great job! You finished $workoutTitle in $duration min and burned $calories kcal.")
            .setPositiveButton("Finish & Save") { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .setCancelable(false)
            .show()
    }

    private fun showExitConfirmation() {
        AlertDialog.Builder(this)
            .setTitle("Quit Workout?")
            .setMessage("Are you sure you want to leave? Your active set progress will be saved.")
            .setPositiveButton("Quit") { _, _ ->
                cancelTimer()
                finish()
            }
            .setNegativeButton("Resume", null)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelTimer()
    }
}
