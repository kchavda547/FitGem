package com.example.fitgem

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.fitgem.data.database.FitGenDatabase
import com.example.fitgem.data.entity.UserEntity
import com.example.fitgem.databinding.ActivityEditProfileBinding
import com.example.fitgem.utils.SessionManager
import com.example.fitgem.utils.ValidationUtils
import kotlinx.coroutines.launch

/**
 * EDIT PROFILE SCREEN
 *
 * - Allows user to edit name, mobile, age, height, weight, and fitness goal
 * - Updates user in Room DB and updates SessionManager
 */
class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var session: SessionManager
    private var currentUser: UserEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = SessionManager(this)

        binding.tvBack.setOnClickListener { finish() }
        binding.btnSaveChanges.setOnClickListener { saveProfileChanges() }

        loadUserData()
    }

    private fun loadUserData() {
        val userId = session.getUserId()
        lifecycleScope.launch {
            val db = FitGenDatabase.getDatabase(this@EditProfileActivity)
            currentUser = db.userDao().getUserById(userId)

            runOnUiThread {
                currentUser?.let { user ->
                    binding.etName.setText(user.name)
                    binding.etEmail.setText(user.email)
                    binding.etMobile.setText(user.mobile)
                    if (user.age > 0) binding.etAge.setText(user.age.toString())
                    if (user.heightCm > 0) binding.etHeight.setText(user.heightCm.toString())
                    if (user.weightKg > 0) binding.etWeight.setText(user.weightKg.toString())
                    binding.etFitnessGoal.setText(user.fitnessGoal)
                }
            }
        }
    }

    private fun saveProfileChanges() {
        val name     = binding.etName.text.toString().trim()
        val mobile   = binding.etMobile.text.toString().trim()
        val ageStr   = binding.etAge.text.toString().trim()
        val heightStr= binding.etHeight.text.toString().trim()
        val weightStr= binding.etWeight.text.toString().trim()
        val goal     = binding.etFitnessGoal.text.toString().trim()

        // Validate
        ValidationUtils.validateRequired(name, "Name")?.let {
            binding.tilName.error = it
            return
        }
        binding.tilName.error = null

        val age    = ageStr.toIntOrNull() ?: 0
        val height = heightStr.toFloatOrNull() ?: 0f
        val weight = weightStr.toFloatOrNull() ?: 0f

        val user = currentUser ?: return
        val updatedUser = user.copy(
            name        = name,
            mobile      = mobile,
            age         = age,
            heightCm    = height,
            weightKg    = weight,
            fitnessGoal = if (goal.isNotEmpty()) goal else "Stay Fit"
        )

        lifecycleScope.launch {
            val db = FitGenDatabase.getDatabase(this@EditProfileActivity)
            db.userDao().updateUser(updatedUser)

            // Update session user name
            session.updateUserName(name)

            runOnUiThread {
                Toast.makeText(this@EditProfileActivity, "Profile updated successfully! ✅", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
