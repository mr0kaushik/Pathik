package com.pathik.ride.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.pathik.ride.R
import com.pathik.ride.databinding.ActivitySplashBinding
import com.pathik.ride.ui.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SplashActivity : AppCompatActivity(), Animation.AnimationListener {

    companion object {
        private const val ANIMATION_DURATION = 1500L
    }
    private lateinit var binding: ActivitySplashBinding

    private var hasLoggedIn: Boolean? = null


    override fun onCreate(savedInstanceState: Bundle?) {
//        setTheme(R.style.Theme_Pathik)
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val scaleAnimation = ScaleAnimation(
            1f,
            10f,
            1f,
            10f,
            ScaleAnimation.RELATIVE_TO_SELF,
            0.5f,
            ScaleAnimation.RELATIVE_TO_SELF,
            0.5F
        );
        scaleAnimation.duration = ANIMATION_DURATION;
        scaleAnimation.fillAfter = true;
        scaleAnimation.setAnimationListener(this)
        binding.cvDark.animation = scaleAnimation
    }

    override fun onResume() {
        super.onResume()
        hasLoggedIn = false
    }


    override fun onAnimationRepeat(animation: Animation?) {}
    override fun onAnimationStart(animation: Animation) {}
    override fun onAnimationEnd(animation: Animation) {
        Handler(Looper.getMainLooper()).postDelayed({
            if (hasLoggedIn == true) {
                Timber.i("User has Logged In")
//                val action = SplashActivityDirections.actionSplashActivityToMainActivity()
//                navController.navigate(action)
            } else {
                Timber.i("User is not logged in")
                startActivity(Intent(this, AuthActivity::class.java))
            }
        }, 200)
    }

}