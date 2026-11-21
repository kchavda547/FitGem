package com.example.fitgem.model

import com.example.fitgem.R

/**
 * WORKOUT DATA — pre-defined list of all workout plans.
 *
 * In a production app this would come from a server, but for a college
 * project we define the plans here. The data references drawable images
 * (a.png through n.png) that are already in the res/drawable folder.
 */
object WorkoutData {

    /** Returns the full list of all workout plans. */
    fun getAllWorkouts(): List<WorkoutModel> = listOf(
        fullBodyWorkout(),
        upperBodyWorkout(),
        legWorkout(),
        coreWorkout(),
        cardioWorkout()
    )

    private fun fullBodyWorkout() = WorkoutModel(
        id = 1,
        name = "Full Body Workout",
        category = "Beginner",
        durationMins = 35,
        exerciseCount = 5,
        caloriesBurned = 280,
        imageResId = R.drawable.b,
        exercises = listOf(
            ExerciseModel("Jumping Jacks",  "30 sec",  30, R.drawable.c),
            ExerciseModel("Push Ups",       "12 reps",  0, R.drawable.d),
            ExerciseModel("Squats",         "15 reps",  0, R.drawable.e),
            ExerciseModel("Lunges",         "12 reps",  0, R.drawable.f),
            ExerciseModel("Plank",          "30 sec",  30, R.drawable.g)
        )
    )

    private fun upperBodyWorkout() = WorkoutModel(
        id = 2,
        name = "Upper Body Workout",
        category = "Intermediate",
        durationMins = 40,
        exerciseCount = 6,
        caloriesBurned = 320,
        imageResId = R.drawable.h,
        exercises = listOf(
            ExerciseModel("Push Ups",         "15 reps",  0, R.drawable.d),
            ExerciseModel("Dips",             "12 reps",  0, R.drawable.i),
            ExerciseModel("Shoulder Press",   "12 reps",  0, R.drawable.j),
            ExerciseModel("Bicep Curls",      "15 reps",  0, R.drawable.k),
            ExerciseModel("Tricep Extension", "12 reps",  0, R.drawable.l),
            ExerciseModel("Plank",            "45 sec",  45, R.drawable.g)
        )
    )

    private fun legWorkout() = WorkoutModel(
        id = 3,
        name = "Leg Day Workout",
        category = "Intermediate",
        durationMins = 45,
        exerciseCount = 6,
        caloriesBurned = 350,
        imageResId = R.drawable.m,
        exercises = listOf(
            ExerciseModel("Squats",         "20 reps",  0, R.drawable.e),
            ExerciseModel("Lunges",         "15 reps",  0, R.drawable.f),
            ExerciseModel("Leg Press",      "12 reps",  0, R.drawable.n),
            ExerciseModel("Calf Raises",    "20 reps",  0, R.drawable.c),
            ExerciseModel("Wall Sit",       "45 sec",  45, R.drawable.d),
            ExerciseModel("Jump Squats",    "15 reps",  0, R.drawable.e)
        )
    )

    private fun coreWorkout() = WorkoutModel(
        id = 4,
        name = "Core & Abs",
        category = "Beginner",
        durationMins = 20,
        exerciseCount = 5,
        caloriesBurned = 180,
        imageResId = R.drawable.a,
        exercises = listOf(
            ExerciseModel("Crunches",     "20 reps",  0, R.drawable.c),
            ExerciseModel("Leg Raises",   "15 reps",  0, R.drawable.d),
            ExerciseModel("Plank",        "30 sec",  30, R.drawable.g),
            ExerciseModel("Russian Twist","20 reps",  0, R.drawable.e),
            ExerciseModel("Bicycle Kick", "20 reps",  0, R.drawable.f)
        )
    )

    private fun cardioWorkout() = WorkoutModel(
        id = 5,
        name = "Cardio Blast",
        category = "Advanced",
        durationMins = 30,
        exerciseCount = 6,
        caloriesBurned = 400,
        imageResId = R.drawable.b,
        exercises = listOf(
            ExerciseModel("Jumping Jacks",   "1 min",   60, R.drawable.c),
            ExerciseModel("High Knees",      "1 min",   60, R.drawable.d),
            ExerciseModel("Burpees",         "10 reps",  0, R.drawable.e),
            ExerciseModel("Mountain Climbers","45 sec", 45, R.drawable.f),
            ExerciseModel("Jump Rope (sim)", "1 min",   60, R.drawable.g),
            ExerciseModel("Sprint in Place", "30 sec",  30, R.drawable.h)
        )
    )
}
