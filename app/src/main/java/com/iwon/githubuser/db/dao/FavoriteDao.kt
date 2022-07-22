package com.iwon.githubuser.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.iwon.githubuser.db.entity.Favorite

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favorite: Favorite)

    @Delete
    fun delete(favorite: Favorite)

    @Query("SELECT * FROM favorite ORDER BY id DESC")
    fun getListFavorite() : LiveData<List<Favorite>>
}