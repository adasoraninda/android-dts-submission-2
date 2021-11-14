package com.adasoraninda.githubuserdts.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adasoraninda.githubuserdts.common.ListUserAdapter
import com.adasoraninda.githubuserdts.databinding.ActivityListUserBinding
import com.adasoraninda.githubuserdts.utils.obtainViewModel
import com.adasoraninda.githubuserdts.utils.showToastMessage
import com.adasoraninda.githubuserdts.viewmodel.ListUserViewModel

private const val TAG = "ListUserActivity"

class ListUserActivity : AppCompatActivity() {

    private var _binding: ActivityListUserBinding? = null
    private val binding get() = _binding

    private val listUserAdapter by lazy { ListUserAdapter(this::navigateToDetailUser) }

    private lateinit var viewModel: ListUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityListUserBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        viewModel = obtainViewModel(ListUserViewModel::class.java)

        initListUsers()
        initSearch()
        initSwipeRefresh()

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.users.observe(this) {
            Log.d(TAG, "$it")
            listUserAdapter.users = it
        }

        viewModel.error.observe(this) {
            binding?.textError?.isVisible = it
        }

        viewModel.loading.observe(this) {
            binding?.progressBar?.isVisible = it
        }

        viewModel.refresh.observe(this) {
            binding?.swipeRefresh?.isRefreshing = it
        }
    }

    private fun initSwipeRefresh() {
        binding?.swipeRefresh?.setOnRefreshListener {
            viewModel.onRefresh()
        }
    }

    private fun initSearch() {
        binding?.searchUsers?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    viewModel.onQuerySubmit(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun initListUsers() {
        binding?.layoutList?.listUsers?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.layoutList?.listUsers?.adapter = listUserAdapter
        binding?.layoutList?.listUsers?.addItemDecoration(
            DividerItemDecoration(
                this,
                RecyclerView.VERTICAL
            )
        )
        binding?.layoutList?.listUsers?.setHasFixedSize(true)
    }

    private fun navigateToDetailUser(user: String?) {
        showToastMessage("$user")
//        val intent = Intent(this, DetailUserActivity::class.java)
//        intent.putExtra(DetailUserActivity.EXTRA_USER_NAME, user)
//        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}