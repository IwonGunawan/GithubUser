package com.iwon.githubuser.page.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iwon.githubuser.GlobalVariable
import com.iwon.githubuser.R
import com.iwon.githubuser.api.response.ListUsersResponse
import de.hdodenhof.circleimageview.CircleImageView

class ListUserAdapter(private val mContext : Context ,private val listUsers  : List<ListUsersResponse>) : RecyclerView.Adapter<ListUserAdapter.ViewHolder>() {
    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val itemMain : ConstraintLayout = view.findViewById(R.id.item_main)
        val imgAvatar : CircleImageView = view.findViewById(R.id.img_avatar)
        val tvName : TextView = view.findViewById(R.id.tv_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_main, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = listUsers[position].login
        Glide.with(mContext)
            .load(listUsers[position].avatarUrl)
            .into(holder.imgAvatar)
        holder.itemMain.setOnClickListener {
            Log.d(GlobalVariable.TAG, "onBindViewHolder: ${listUsers[position].login}")
        }
    }

    override fun getItemCount() = listUsers.size
}