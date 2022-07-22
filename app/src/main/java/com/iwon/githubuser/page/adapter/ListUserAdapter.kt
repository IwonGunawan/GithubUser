package com.iwon.githubuser.page.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iwon.githubuser.GlobalVariable
import com.iwon.githubuser.GlobalVariable.Companion.loadImage
import com.iwon.githubuser.R
import com.iwon.githubuser.api.response.ListUsersResponse
import com.iwon.githubuser.db.entity.Favorite
import com.iwon.githubuser.page.viewModel.FavoriteViewModel
import de.hdodenhof.circleimageview.CircleImageView

class ListUserAdapter(private val mContext: Context ,private val listUsers: List<ListUsersResponse>) : RecyclerView.Adapter<ListUserAdapter.ViewHolder>() {

    var callbackListener : CallbackListener? = null

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val itemMain : ConstraintLayout = view.findViewById(R.id.item_main)
        val imgAvatar : CircleImageView = view.findViewById(R.id.img_avatar)
        val tvName : TextView = view.findViewById(R.id.tv_name)
        val ivFavorite : ImageView = view.findViewById(R.id.iv_favorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_main, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = listUsers[position]

        holder.tvName.text = data.login
        holder.imgAvatar.loadImage(data.avatarUrl)
        holder.itemMain.setOnClickListener {
            callbackListener?.onClick(data)
        }
        holder.ivFavorite.setOnClickListener {
            callbackListener?.onFavorite(data)
        }
    }

    override fun getItemCount() = listUsers.size

    interface CallbackListener{
        fun onClick(user : ListUsersResponse)
        fun onFavorite(user : ListUsersResponse)
    }
}