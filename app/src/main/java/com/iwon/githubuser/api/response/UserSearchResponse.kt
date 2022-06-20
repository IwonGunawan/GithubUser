package com.iwon.githubuser.api.response

import com.google.gson.annotations.SerializedName

data class UserSearchResponse(

    @field:SerializedName("total_count")
    val totalCount : Int,

    @field:SerializedName("items")
    val items : List<ListUsersResponse>
)
