package com.iwon.githubuser.db.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.iwon.githubuser.GlobalVariable
import com.iwon.githubuser.api.ApiService
import com.iwon.githubuser.api.response.ListUsersResponse
import com.iwon.githubuser.db.dao.UserDao
import com.iwon.githubuser.db.entity.UserEntity
import com.iwon.githubuser.helper.AppExecutors
import com.iwon.githubuser.helper.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val appExecutors : AppExecutors
){
    private val result = MediatorLiveData<Result<List<UserEntity>>>()

    fun getListUser() : LiveData<Result<List<UserEntity>>>{
        result.value = Result.Loading
        val client = apiService.getListUsers(GlobalVariable.headerAccept, GlobalVariable.headerAuth)
        client.enqueue(object : Callback<List<ListUsersResponse>>{
            override fun onResponse(
                call: Call<List<ListUsersResponse>>,
                response: Response<List<ListUsersResponse>>
            ) {
                if (response.code() == GlobalVariable.iRESPONSE_OK && response.body() != null){
                    val userResp = response.body()
                    appExecutors.diskIO.execute {
                        val userList = ArrayList<UserEntity>()
                        userResp?.forEach { user ->
                            val stateUserBookmark = userDao.isBookmark(user.id)
                            val entity = UserEntity(
                                user.id,
                                user.login,
                                user.avatarUrl,
                                user.url,
                                stateUserBookmark)
                            userList.add(entity)
                        }
                        userDao.deleteAll()
                        userDao.insertAllUser(userList)
                    } // end appExecutor
                } // end 200
            }

            override fun onFailure(call: Call<List<ListUsersResponse>>, t: Throwable) {
                result.value = Result.Error(t)
            }
        }) // end enqueue

        val localData = userDao.getListUser()
        result.addSource(localData){ newData : List<UserEntity> ->
            result.value = Result.Success(newData)
        }

        return result
    }

    fun setFavorite(userEntity: UserEntity, state : Boolean){
        appExecutors.diskIO.execute {
            userEntity.isBoomark = state
            userDao.updateUser(userEntity)
        }
    }

    fun getFavorite() : LiveData<List<UserEntity>>{
        return userDao.getFavorite()
    }

    companion object{
        @Volatile
        private var instance : UserRepository?  = null

        fun getInstance(
            apiService: ApiService,
            userDao: UserDao,
            appExecutors: AppExecutors
        ) : UserRepository =
            instance ?: synchronized(this){
                instance ?: UserRepository(apiService, userDao, appExecutors)
            }.also {
                instance = it
            }
    }


}