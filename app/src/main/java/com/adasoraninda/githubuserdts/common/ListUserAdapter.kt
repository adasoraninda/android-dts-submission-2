package com.adasoraninda.githubuserdts.common

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adasoraninda.githubuserdts.data.domain.User
import com.adasoraninda.githubuserdts.databinding.ItemUserBinding
import com.bumptech.glide.Glide

class ListUserAdapter(
    private val itemClickListener: (username: String) -> Unit
) : RecyclerView.Adapter<ListUserAdapter.UserViewHolder>() {

    var users = listOf<User>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)
        return UserViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bindData(users[position])
    }

    override fun getItemCount(): Int {
        return users.size
    }

    inner class UserViewHolder(
        private val binding: ItemUserBinding,
        private val itemClickListener: (username: String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(data: User) {
            binding.textUsername.text = data.username

            Glide.with(itemView.context)
                .load(data.photo)
                .into(binding.imageUser)

            binding.root.setOnClickListener { itemClickListener(data.username) }
        }

    }

}