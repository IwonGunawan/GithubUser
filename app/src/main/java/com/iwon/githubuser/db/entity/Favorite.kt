package com.iwon.githubuser.db.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favorite")
@Parcelize
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id : Int = 0,

    @ColumnInfo(name = "user_id")
    var userId : Int? = 0,

    @ColumnInfo(name = "username")
    var userName : String? = null,

    @ColumnInfo(name ="avatar_url")
    var avatarUrl : String? = null,

    @ColumnInfo(name = "link_url")
    var linkUrl : String? = null
) : Parcelable
