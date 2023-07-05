package com.alvaro.suitemdiamd.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alvaro.suitemdiamd.data.api.UserResponseItem
import com.alvaro.suitemdiamd.databinding.UserCardBinding
import com.bumptech.glide.Glide

class SelectedUserList(
    private val onClick: (String) -> Unit
) : PagingDataAdapter<UserResponseItem, SelectedUserList.ListViewHolder>(DIFF_CALLBACK) {

    inner class ListViewHolder(private val binding: UserCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: UserResponseItem) {
            Glide.with(binding.ivUserPhoto.context)
                .load(data.avatar)
                .circleCrop()
                .into(binding.ivUserPhoto)
            binding.tvUserName.text = String.format("%s %s", data.firstName, data.lastName)
            binding.tvUserEmail.text = data.email
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = UserCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        data?.let {
            holder.bind(it)
            holder.itemView.setOnClickListener {
                onClick("${data.firstName} ${data.lastName}")
            }
        }
    }



    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserResponseItem>() {
            override fun areItemsTheSame(
                oldItem: UserResponseItem,
                newItem: UserResponseItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: UserResponseItem,
                newItem: UserResponseItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}