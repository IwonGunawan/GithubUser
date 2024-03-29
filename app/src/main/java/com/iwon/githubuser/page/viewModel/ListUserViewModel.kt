package com.iwon.githubuser.page.viewModel

import androidx.lifecycle.ViewModel
import com.iwon.githubuser.db.entity.UserEntity
import com.iwon.githubuser.db.repository.UserRepository

class ListUserViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getListUser() = userRepository.getListUser()

    fun setFavorite(userEntity: UserEntity) = userRepository.setFavorite(userEntity, true)

    fun unFavorite(userEntity: UserEntity) = userRepository.setFavorite(userEntity, false)

    fun getFavorite() = userRepository.getFavorite()

    fun insertUser(userEntity: UserEntity) = userRepository.insertUsers(userEntity)

    fun isFavorite(userId: Int) = userRepository.isFavorite(userId)

    fun isExist(userId: Int) = userRepository.isExist(userId)
}