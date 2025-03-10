package com.dayeeen.mystoryapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dayeeen.mystoryapp.R
import com.dayeeen.mystoryapp.databinding.ActivityRegisterBinding
import com.dayeeen.mystoryapp.utils.StateResult
import com.dayeeen.mystoryapp.viewmodel.RegisterViewModel
import com.dayeeen.mystoryapp.viewmodel.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val rvm by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignup.setOnClickListener {
            userRegister()
        }

        binding.tvHaveAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun userRegister() {
        val name = binding.edRegisterName.text.toString()
        val email = binding.edRegisterEmail.text.toString()
        val password = binding.edRegisterPassword.text.toString()
        rvm.registerUser(name = name, email = email, password = password)
            .observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is StateResult.InProgress -> {
                            showLoading(true)
                        }

                        is StateResult.Success -> {
                            showLoading(false)
                            AlertDialog.Builder(this).apply {
                                setTitle(R.string.register_success)
                                setMessage(R.string.register_success_message)
                                setPositiveButton(R.string.cont) { _, _ ->
                                    // Langsung diarahkan ke halaman login
                                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                create()
                                show()
                            }
                        }

                        is StateResult.Failure -> {
                            showToast(result.message)
                            showLoading(false)
                        }
                    }
                }
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}
