package com.iwon.githubuser.api.response

import com.google.gson.annotations.SerializedName

data class ListUsersResponse(

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("url")
	val url: String
)