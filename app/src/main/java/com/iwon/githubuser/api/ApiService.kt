package com.iwon.githubuser.api

import com.iwon.githubuser.api.response.ListUsersResponse
import com.iwon.githubuser.api.response.UserResponse
import com.iwon.githubuser.api.response.UserSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    fun getListUsers(
        @Header("Accept") accept : String,
        @Header("Authorization") auth : String
    ) : Call<List<ListUsersResponse>>

    @GET("users/{username}")
    fun getUser(
        @Header("Accept") accept : String,
        @Header("Authorization") auth : String,
        @Path("username") username : String
    ) : Call<UserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Header("Accept") accept: String,
        @Header("Authorization") auth: String,
        @Path("username") username: String
    ) : Call<List<ListUsersResponse>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Header("Accept") accept: String,
        @Header("Authorization") auth: String,
        @Path("username") username: String
    ) : Call<List<ListUsersResponse>>

    @GET("search/users")
    fun searchUser(
        @Header("Authorization") auth: String,
        @Query("q") q: String
    ) : Call<UserSearchResponse>

}