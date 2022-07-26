package com.iwon.githubuser.helper

import android.content.Context
import com.iwon.githubuser.api.ApiConfig
import com.iwon.githubuser.db.GithubUserDB
import com.iwon.githubuser.db.repository.UserRepository

object Injection {
    fun provideRepository(context: Context) : UserRepository{
        val apiService = ApiConfig.getApiService()
        val database = GithubUserDB.getDatabase(context)
        val dao = database.userDao()
        val appExecutors = AppExecutors()

        return UserRepository.getInstance(apiService, dao, appExecutors)
    }
}