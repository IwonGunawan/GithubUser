package com.iwon.githubuser.db.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.iwon.githubuser.db.GithubUserDB
import com.iwon.githubuser.db.dao.FavoriteDao
import com.iwon.githubuser.db.entity.Favorite
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {

    private val mFavoriteDao : FavoriteDao
    private val executorService : ExecutorService = Executors.newSingleThreadExecutor()


    init {
        val db = GithubUserDB.getDatabase(application)
        mFavoriteDao = db.favoriteDao()
    }

    fun insert(favorite: Favorite) = executorService.execute { mFavoriteDao.insert(favorite) }

    fun delete(favorite: Favorite) = executorService.execute { mFavoriteDao.delete(favorite) }

    fun getListFavorite() : LiveData<List<Favorite>> = mFavoriteDao.getListFavorite()
}