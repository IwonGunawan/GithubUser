package com.iwon.githubuser.page.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.iwon.githubuser.GlobalVariable
import com.iwon.githubuser.GlobalVariable.Companion.loadImage
import com.iwon.githubuser.R
import com.iwon.githubuser.api.response.ListUsersResponse
import com.iwon.githubuser.db.entity.UserEntity
import com.iwon.githubuser.page.viewModel.ListUserViewModel
import de.hdodenhof.circleimageview.CircleImageView

class SearchUserAdapter(private val mContext: Context, private val listUsers: List<ListUsersResponse>, private val listUserViewModel: ListUserViewModel) : RecyclerView.Adapter<SearchUserAdapter.ViewHolder>() {

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

        val isFavorite = listUserViewModel.isFavorite(data.id)
        if (isFavorite){
            holder.ivFavorite.setImageDrawable(ContextCompat.getDrawable(holder.ivFavorite.context, R.drawable.ic_favorite_selected))
        }else{
            holder.ivFavorite.setImageDrawable(ContextCompat.getDrawable(holder.ivFavorite.context, R.drawable.ic_favorite_white))
        }

        holder.itemMain.setOnClickListener {
            callbackListener?.onClick(data)
        }
        holder.ivFavorite.setOnClickListener {
            Log.d(GlobalVariable.TAG, "onBindViewHolder: onClick favorite ${data.id}")
            val entity = UserEntity(
                data.id,
                data.login,
                data.avatarUrl,
                data.url,
                true)
            val isExist = listUserViewModel.isExist(data.id)
            if (isExist){
                // update
                val checkFavorite = listUserViewModel.isFavorite(data.id)
                if (checkFavorite){
                    listUserViewModel.unFavorite(entity)
                }else{
                    listUserViewModel.setFavorite(entity)
                }
            }else{
                // insert
                listUserViewModel.insertUser(entity)
            }

            notifyDataSetChanged()
        }
    }

    override fun getItemCount() = listUsers.size

    interface CallbackListener{
        fun onClick(user : ListUsersResponse)
    }
}