package com.iwon.githubuser.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.iwon.githubuser.db.dao.FavoriteDao
import com.iwon.githubuser.db.entity.Favorite

@Database(entities = [Favorite::class], version = 1)
abstract class GithubUserDB : RoomDatabase() {

    abstract fun favoriteDao() : FavoriteDao

    companion object{
        @Volatile
        private var INSTANCE : GithubUserDB? = null

        @JvmStatic
        fun getDatabase(context: Context) : GithubUserDB{
            if (INSTANCE == null){
                synchronized(GithubUserDB::class.java){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        GithubUserDB::class.java,
                        "github_user_db"
                    ).build()
                }
            }

            return INSTANCE as GithubUserDB
        }
    }



}