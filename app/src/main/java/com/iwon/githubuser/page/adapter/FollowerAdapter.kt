package com.iwon.githubuser.page.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.iwon.githubuser.GlobalVariable.Companion.loadImage
import com.iwon.githubuser.R
import com.iwon.githubuser.api.response.ListUsersResponse
import de.hdodenhof.circleimageview.CircleImageView

class FollowerAdapter(private val mContext: Context, private val list: List<ListUsersResponse>) : RecyclerView.Adapter<FollowerAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgAvatar : CircleImageView = view.findViewById(R.id.img_avatar)
        val tvUsername : TextView = view.findViewById(R.id.tv_username)
        val tvHtmlUrl : TextView = view.findViewById(R.id.tv_html_url)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_follower, parent, false))

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imgAvatar.loadImage(list[position].avatarUrl)
        holder.tvUsername.text = list[position].login
        holder.tvHtmlUrl.text = list[position].url
    }
}