package com.iwon.githubuser.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class UserEntity(

    @field:ColumnInfo(name = "user_id")
    @field:PrimaryKey
    val userId : Int? = 0,

    @field:ColumnInfo(name = "username")
    val userName : String,

    @field:ColumnInfo(name = "avatar_url")
    val avatarUrl : String,

    @field:ColumnInfo(name = "link_url")
    val linkUrl : String,

    @field:ColumnInfo(name = "is_bookmark")
    var isBoomark : Boolean
)
