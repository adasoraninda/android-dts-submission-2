package com.adasoraninda.githubuserdts.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.adasoraninda.githubuserdts.R
import com.adasoraninda.githubuserdts.data.domain.User
import com.adasoraninda.githubuserdts.databinding.ActivityDetailUserBinding
import com.bumptech.glide.Glide


class DetailUserActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER_NAME = "EXTRA_USER_NAME"
    }

    private var _binding: ActivityDetailUserBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail_user, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_share -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun shareUser(user: User?) {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_SUBJECT, "Share GitHub User")
            putExtra(Intent.EXTRA_TEXT, "$user")
        }.apply {
            startActivity(Intent.createChooser(this, "Send data"))
        }
    }

    private fun initData(user: User?) {
        binding?.textUsername?.text = user?.username
        binding?.textName?.text = user?.name
        binding?.imageUser?.let {
            Glide.with(this)
                .load(user?.photo)
                .into(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}