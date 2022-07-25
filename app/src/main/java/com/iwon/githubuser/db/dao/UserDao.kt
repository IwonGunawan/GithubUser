package com.iwon.githubuser.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.iwon.githubuser.db.entity.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getListUser() : LiveData<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user : UserEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllUser(listUser : List<UserEntity>)

    @Update
    fun updateUser(userEntity: UserEntity)

    @Query("DELETE FROM user WHERE 1")
    fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM user WHERE user_id=:userId AND is_bookmark=1)")
    fun isBookmark(userId : Int) : Boolean

    @Query("SELECT * FROM user WHERE is_bookmark=1")
    fun getFavorite() : LiveData<List<UserEntity>>
}