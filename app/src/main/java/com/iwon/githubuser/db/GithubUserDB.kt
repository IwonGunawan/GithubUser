package com.iwon.githubuser.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.iwon.githubuser.db.dao.UserDao
import com.iwon.githubuser.db.entity.UserEntity

@Database(entities = [UserEntity::class], version = 1, exportSchema = false)
abstract class GithubUserDB : RoomDatabase() {

    abstract fun userDao() : UserDao

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