package com.adasoraninda.githubuserdts.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.adasoraninda.githubuserdts.R
import com.adasoraninda.githubuserdts.common.FollowType
import com.adasoraninda.githubuserdts.common.SectionFollowAdapter
import com.adasoraninda.githubuserdts.data.domain.User
import com.adasoraninda.githubuserdts.databinding.ActivityDetailUserBinding
import com.adasoraninda.githubuserdts.utils.obtainViewModel
import com.adasoraninda.githubuserdts.viewmodel.DetailUserViewModel
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator

private const val TAG = "DetailUserActivity"

class DetailUserActivity : AppCompatActivity() {

    private var _binding: ActivityDetailUserBinding? = null
    private val binding get() = _binding

    private val bindingHeader get() = binding?.layoutHeader

    private lateinit var viewModel: DetailUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val intent = intent?.extras
        val username = intent?.getString(EXTRA_USERNAME)

        viewModel = obtainViewModel(DetailUserViewModel::class.java)

        if (savedInstanceState == null) {
            viewModel.getDetailUser(username)
        }

        initActionBar(username)
        initTabSection()

        observeViewModel()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val username = intent?.getStringExtra(EXTRA_USERNAME)
        viewModel.getDetailUser(username)

        initActionBar(username)
        initTabSection()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail_user, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                viewModel.onBackClicked()
                true
            }
            R.id.action_share -> {
                viewModel.shareUser()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initActionBar(title: String?) {
        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.elevation = 0F
    }

    private fun initData(user: User?) {
        bindingHeader?.apply {
            textUsername.text = user?.username
            textName.text = user?.name
            imageUser.let {
                Glide.with(it.context)
                    .load(user?.photo)
                    .into(it)
            }
        }
    }

    private fun initTabSection() {
        val tabLayout = binding?.tabLayout
        val viewPager = binding?.viewPager?.apply {
            adapter = SectionFollowAdapter(this@DetailUserActivity)
        }

        if (tabLayout != null && viewPager != null) {
            TabLayoutMediator(tabLayout, viewPager) { tab, i ->
                tab.text = getString(FollowType.values()[i].title)
            }.attach()
        }
    }

    private fun observeViewModel() {
        viewModel.user.observe(this) {
            Log.d(TAG, "$it")
            initData(it)
        }

        viewModel.share.observe(this) { event ->
            val result = event.getContent()
            result?.let { shareUser(it) }
        }

        viewModel.back.observe(this) { event ->
            val result = event.getContent() ?: false
            if (result) onBackPressed()
        }

        viewModel.error.observe(this) {
            if (it) {
                Toast.makeText(this, "Please try again later", Toast.LENGTH_SHORT).show()
                viewModel.onBackClicked()
            }
        }

        viewModel.loading.observe(this) {
            binding?.layoutShimmer?.root?.isVisible = it
            binding?.progressBar?.isVisible = it
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val EXTRA_USERNAME = "EXTRA_USERNAME"
    }

}