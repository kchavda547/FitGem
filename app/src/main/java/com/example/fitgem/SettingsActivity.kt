package com.example.fitgem

import android.os.Bundle
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fitgem.utils.SessionManager

class SettingsActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager

    private lateinit var btnBack: ImageView
    private lateinit var rgThemeSelector: RadioGroup
    private lateinit var rbThemeLight: RadioButton
    private lateinit var rbThemeDark: RadioButton
    private lateinit var rbThemeSystem: RadioButton

    private lateinit var rgUnits: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(this)
        sessionManager.applyThemeMode()

        setContentView(R.layout.activity_settings)

        btnBack = findViewById(R.id.btnBack)
        rgThemeSelector = findViewById(R.id.rgThemeSelector)
        rbThemeLight = findViewById(R.id.rbThemeLight)
        rbThemeDark = findViewById(R.id.rbThemeDark)
        rbThemeSystem = findViewById(R.id.rbThemeSystem)
        rgUnits = findViewById(R.id.rgUnits)

        btnBack.setOnClickListener { finish() }

        // Set initial theme selection
        when (sessionManager.getThemeMode()) {
            SessionManager.THEME_LIGHT -> rbThemeLight.isChecked = true
            SessionManager.THEME_DARK -> rbThemeDark.isChecked = true
            SessionManager.THEME_SYSTEM -> rbThemeSystem.isChecked = true
        }

        rgThemeSelector.setOnCheckedChangeListener { _, checkedId ->
            val mode = when (checkedId) {
                R.id.rbThemeLight -> SessionManager.THEME_LIGHT
                R.id.rbThemeDark -> SessionManager.THEME_DARK
                R.id.rbThemeSystem -> SessionManager.THEME_SYSTEM
                else -> SessionManager.THEME_LIGHT
            }

            if (mode != sessionManager.getThemeMode()) {
                sessionManager.setThemeMode(mode)
                Toast.makeText(this, "Theme updated!", Toast.LENGTH_SHORT).show()
                recreate()
            }
        }
    }
}
