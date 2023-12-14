package com.example.storyapp.userinterface

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.UserPreferences
import com.example.storyapp.databinding.ActivitySplashScreenBinding
import com.example.storyapp.viewmodel.UserLoginViewModel
import com.example.storyapp.viewmodel.ViewModelFactory

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserPreferences.getInstance(dataStore)
        val loginViewModel =
            ViewModelProvider(this, ViewModelFactory(pref))[UserLoginViewModel::class.java]

        loginViewModel.getLoginSession().observe(this) { isLoggedIn ->

            val splashScreenTextLogo =
                ObjectAnimator.ofFloat(binding.tvSplashScreen, View.ALPHA, 1f).setDuration(3000)
            val splashScreenTextBottom =
                ObjectAnimator.ofFloat(binding.bottomSplashScreen, View.ALPHA, 1f).setDuration(3000)
            val splashlogoapk =
                ObjectAnimator.ofFloat(binding.logoapk, View.ALPHA, 1f). setDuration(3000)

            AnimatorSet().apply {
                playTogether(splashScreenTextBottom, splashScreenTextLogo,splashlogoapk)
                start()
                addListener(object: AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                })
            }

            // Check login status before starting MainActivity or HomeActivity
            val intent = if (isLoggedIn) {
                Intent(this, HomePageActivity::class.java)
            } else {
                Intent(this, MainActivity::class.java)
            }
        }
    }
}