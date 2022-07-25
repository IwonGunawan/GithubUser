package com.iwon.githubuser.page.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.iwon.githubuser.GlobalVariable.Companion.loadImage
import com.iwon.githubuser.R
import com.iwon.githubuser.databinding.ItemFavoriteBinding
import com.iwon.githubuser.db.entity.UserEntity
import com.iwon.githubuser.page.adapter.ListUserAdapter.Companion.DIFF_CALLBACK

class ListFavoriteAdapter(private val onFavorite : (UserEntity) -> Unit) : ListAdapter<UserEntity, ListFavoriteAdapter.MyViewHolder>(DIFF_CALLBACK)  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var users = getItem(position)
        holder.bind(users)

        val ivFavorite = holder.binding.ivFavorite
        val ivRemove = holder.binding.ivRemove
        ivFavorite.setOnClickListener {
            onFavorite(users)
        }
        ivRemove.setOnClickListener {
            onFavorite(users)
        }
    }

    class MyViewHolder(val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(userEntity: UserEntity){
            binding.imgAvatar.loadImage(userEntity.avatarUrl)
            binding.tvUsername.text = userEntity.userName
            binding.tvHtmlUrl.text = userEntity.linkUrl
        }

    }
}