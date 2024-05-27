package com.dayeeen.mystoryapp.ui.activity


import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dayeeen.mystoryapp.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()

        // Set click listeners
        binding.btnLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnLogin.id -> {
                val intent = Intent(this@StartActivity, LoginActivity::class.java)
                startActivity(intent)
            }
            binding.btnRegister.id -> {
                val intent = Intent(this@StartActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.storyImage, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val btnLogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(100)
        val btnRegister = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(100)
        val description = ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 1f).setDuration(100)

        val together = AnimatorSet().apply {
            playTogether(btnLogin, btnRegister)
        }

        AnimatorSet().apply {
            playSequentially(description, btnLogin, btnRegister, together)
            start()
        }
    }
}
