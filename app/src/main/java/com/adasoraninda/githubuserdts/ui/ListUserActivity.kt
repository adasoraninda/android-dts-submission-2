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
import com.adasoraninda.githubuserdts.navigation.ScreenNavigator
import com.adasoraninda.githubuserdts.utils.obtainViewModel
import com.adasoraninda.githubuserdts.viewmodel.ListUserViewModel

private const val TAG = "ListUserActivity"

class ListUserActivity : AppCompatActivity() {

    private var _binding: ActivityListUserBinding? = null
    private val binding get() = _binding

    private val listUserAdapter by lazy { ListUserAdapter() }

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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun observeViewModel() {
        viewModel.users.observe(this) {
            Log.d(TAG, "$it")
            listUserAdapter.users = it
        }

        viewModel.username.observe(this) { event ->
            val result = event.getContent()
            result?.let { navigateToDetailUser(it) }
        }

        viewModel.error.observe(this) {
            binding?.textError?.isVisible = it
        }

        viewModel.loading.observe(this) {
            binding?.progressBar?.isVisible = it
        }

        viewModel.refresh.observe(this) {
            binding?.swipeRefresh?.isRefreshing = it
            binding?.layoutShimmer?.root?.isVisible = it
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
        binding?.layoutList?.listUsers?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = listUserAdapter.apply {
                setItemOnClickListener { username ->
                    viewModel.onItemClick(username)
                }
            }
            setHasFixedSize(true)
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    RecyclerView.VERTICAL
                )
            )
        }
    }

    private fun navigateToDetailUser(username: String?) {
        val bundle = Bundle().apply { putString(DetailUserActivity.EXTRA_USERNAME, username) }
        ScreenNavigator.navigate(
            context = this,
            destination = DetailUserActivity::class.java,
            bundle = bundle
        )
    }

}