package com.iwon.githubuser.page.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.iwon.githubuser.db.entity.Favorite
import com.iwon.githubuser.db.repository.FavoriteRepository

class FavoriteViewModel(application: Application) : ViewModel() {

    private val mFavoriteRepository : FavoriteRepository = FavoriteRepository(application)

    fun insert(favorite: Favorite){
        mFavoriteRepository.insert(favorite)
    }

    fun delete(favorite: Favorite){
        mFavoriteRepository.delete(favorite)
    }

    fun getListFavorite(){
        mFavoriteRepository.getListFavorite()
    }
}