package com.iwon.githubuser.helper

import androidx.recyclerview.widget.DiffUtil
import com.iwon.githubuser.db.entity.Favorite

class UserDiffCallback(private val oldFavoriteList: List<Favorite>, private val newFavoriteList: List<Favorite>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldFavoriteList.size
    }

    override fun getNewListSize(): Int {
        return newFavoriteList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldFavoriteList[oldItemPosition].id == newFavoriteList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldFavoriteList[oldItemPosition]
        val new = newFavoriteList[newItemPosition]
        return old.userId == new.userId && old.userName == new.userName
    }
}