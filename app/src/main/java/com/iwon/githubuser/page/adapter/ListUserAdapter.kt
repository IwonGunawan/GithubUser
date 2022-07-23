package com.iwon.githubuser.page.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.iwon.githubuser.GlobalVariable.Companion.loadImage
import com.iwon.githubuser.R
import com.iwon.githubuser.databinding.ItemMainBinding
import com.iwon.githubuser.db.entity.UserEntity

class ListUserAdapter(private val onBookmarkClick: (UserEntity) -> Unit) : ListAdapter<UserEntity, ListUserAdapter.MyViewHolder>(DIFF_CALLBACK) {

    lateinit var callbackListener: CallbackListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var users = getItem(position)
        holder.bind(users, callbackListener)

        val ivFavorite = holder.binding.ivFavorite
        if (users.isBoomark){
            ivFavorite.setImageDrawable(ContextCompat.getDrawable(ivFavorite.context, R.drawable.ic_favorite_selected))
        }else{
            ivFavorite.setImageDrawable(ContextCompat.getDrawable(ivFavorite.context, R.drawable.ic_favorite_white))
        }

        ivFavorite.setOnClickListener {
            onBookmarkClick(users)
        }
    }

    class MyViewHolder(val binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(userEntity: UserEntity, callbackListener: CallbackListener){
            binding.tvName.text = userEntity.userName
            binding.imgAvatar.loadImage(userEntity.avatarUrl)
            binding.itemMain.setOnClickListener {
                callbackListener.onClick(userEntity)
            }
        }

    }

    interface CallbackListener{
        fun onClick(userEntity: UserEntity)
    }

    companion object{
        val DIFF_CALLBACK: DiffUtil.ItemCallback<UserEntity> =
            object : DiffUtil.ItemCallback<UserEntity>(){
                override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
                    return oldItem.userId == newItem.userId
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
                    return oldItem == newItem
                }
            }
    }
}
