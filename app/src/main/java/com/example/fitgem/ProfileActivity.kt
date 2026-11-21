package com.example.fitgem

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.fitgem.utils.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    private lateinit var tvAvatarLetter: TextView
    private lateinit var tvProfileName: TextView
    private lateinit var tvProfileGoal: TextView
    private lateinit var tvCurrentThemeVal: TextView

    private lateinit var btnWeightStat: LinearLayout
    private lateinit var btnHeightStat: LinearLayout
    private lateinit var btnBmiStat: LinearLayout

    private lateinit var rowPersonalInfo: LinearLayout
    private lateinit var rowFitnessGoal: LinearLayout
    private lateinit var rowAppearance: LinearLayout
    private lateinit var rowNotifications: LinearLayout
    private lateinit var rowUnits: LinearLayout
    private lateinit var rowSettings: LinearLayout
    private lateinit var rowLogout: LinearLayout

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(this)
        sessionManager.applyThemeMode()

        setContentView(R.layout.activity_profile)

        tvAvatarLetter = findViewById(R.id.tvAvatarLetter)
        tvProfileName = findViewById(R.id.tvProfileName)
        tvProfileGoal = findViewById(R.id.tvProfileGoal)
        tvCurrentThemeVal = findViewById(R.id.tvCurrentThemeVal)

        btnWeightStat = findViewById(R.id.btnWeightStat)
        btnHeightStat = findViewById(R.id.btnHeightStat)
        btnBmiStat = findViewById(R.id.btnBmiStat)

        rowPersonalInfo = findViewById(R.id.rowPersonalInfo)
        rowFitnessGoal = findViewById(R.id.rowFitnessGoal)
        rowAppearance = findViewById(R.id.rowAppearance)
        rowNotifications = findViewById(R.id.rowNotifications)
        rowUnits = findViewById(R.id.rowUnits)
        rowSettings = findViewById(R.id.rowSettings)
        rowLogout = findViewById(R.id.rowLogout)

        bottomNav = findViewById(R.id.bottomNav)

        // Populate User Info
        val name = sessionManager.getUserName()
        tvProfileName.text = name
        if (name.isNotEmpty()) {
            tvAvatarLetter.text = name.first().uppercase()
        }

        val mode = sessionManager.getThemeMode()
        tvCurrentThemeVal.text = when (mode) {
            SessionManager.THEME_DARK -> "Dark ›"
            SessionManager.THEME_SYSTEM -> "System ›"
            else -> "Light ›"
        }

        // Click listeners
        btnBmiStat.setOnClickListener {
            startActivity(Intent(this, BmiActivity::class.java))
        }

        rowPersonalInfo.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        rowFitnessGoal.setOnClickListener {
            startActivity(Intent(this, GoalsActivity::class.java))
        }

        rowAppearance.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        rowNotifications.setOnClickListener {
            Toast.makeText(this, "Notifications: All workout & water reminders active", Toast.LENGTH_SHORT).show()
        }

        rowUnits.setOnClickListener {
            Toast.makeText(this, "Units: Metric System (kg, cm, L)", Toast.LENGTH_SHORT).show()
        }

        rowSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        rowLogout.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out of FITGEM?")
                .setPositiveButton("Logout") { _, _ ->
                    sessionManager.logout()
                    val intent = Intent(this, LoginActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        setupBottomNav()
    }

    private fun setupBottomNav() {
        bottomNav.selectedItemId = R.id.nav_profile

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
                R.id.nav_progress -> {
                    startActivity(Intent(this, ProgressActivity::class.java))
                    overridePendingTransition(0, 0)
                    true
                }
                R.id.nav_profile -> true
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        bottomNav.selectedItemId = R.id.nav_profile
    }
}
