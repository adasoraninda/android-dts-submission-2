package com.adasoraninda.githubuserdts.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.adasoraninda.githubuserdts.R
import kotlinx.coroutines.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        coroutineScope.launch {
            delay(2_000L)
            navigateToList()
        }
    }

    private fun navigateToList() {
        val intent = Intent(this, ListUserActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

}