package com.dayeeen.mystoryapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dayeeen.mystoryapp.data.UserData
import com.dayeeen.mystoryapp.data.di.Injection
import com.dayeeen.mystoryapp.data.retrofit.ApiConfig
import com.dayeeen.mystoryapp.databinding.ActivityLoginBinding
import com.dayeeen.mystoryapp.utils.StateResult
import com.dayeeen.mystoryapp.viewmodel.LoginViewModel
import com.dayeeen.mystoryapp.viewmodel.ViewModelFactory
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val lvm by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvNoAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            login()
        }

    }

    private fun login() {
        val email = binding.edLoginEmail.text.toString()
        val password = binding.edLoginPassword.text.toString()
        lvm.login(email = email, password = password).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is StateResult.InProgress -> {
                        showLoading(true)
                    }

                    is StateResult.Success -> {
                        val token = result.data.loginResult.token
                        lvm.saveSession(UserData(true, email, token))
                        val userRepository = Injection.provideRepository(applicationContext)
                        userRepository.updateApiService(ApiConfig.getApiService(token))
                        AlertDialog.Builder(this).apply {
                            setTitle("Login Successful")
                            setMessage("You have successfully logged in.")
                            setPositiveButton("Continue") { _, _ ->
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                            create().show()
                        }
                        showLoading(false)

                    }
                    is StateResult.Failure -> {
                        showToast(result.message)
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
