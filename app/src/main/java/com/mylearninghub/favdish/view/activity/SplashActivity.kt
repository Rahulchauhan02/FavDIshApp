package com.mylearninghub.favdish.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.mylearninghub.favdish.R
import com.mylearninghub.favdish.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity()
{
    private lateinit var splashBinding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        splashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(splashBinding.root)

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.R)
        {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }
        else
        {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                        )
        }
        val splashAnimation = AnimationUtils.loadAnimation(this,R.anim.splash_anim)
        splashBinding.appName.animation= splashAnimation

        splashAnimation.setAnimationListener(object:Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) {
            //
            }

            override fun onAnimationEnd(p0: Animation?) {
              Handler(Looper.getMainLooper()).postDelayed(
                  {
                      startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        this@SplashActivity.finish()
                  },1000
              )

            }

            override fun onAnimationRepeat(p0: Animation?) {
              //
            }
        })
    }
}