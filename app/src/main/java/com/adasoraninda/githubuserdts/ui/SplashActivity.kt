package com.adasoraninda.githubuserdts.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.adasoraninda.githubuserdts.R
import com.adasoraninda.githubuserdts.navigation.ScreenNavigator
import com.adasoraninda.githubuserdts.utils.obtainViewModel
import com.adasoraninda.githubuserdts.viewmodel.SplashViewModel

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val viewModel = obtainViewModel(SplashViewModel::class.java)

        viewModel.navigation.observe(this) { event ->
            val result = event.getContent()
            result?.let { if (it) navigateToList() }
        }

    }

    private fun navigateToList() {
        ScreenNavigator.navigate(
            context = this,
            destination = ListUserActivity::class.java,
            flag = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        )
    }

}