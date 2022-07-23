package com.iwon.githubuser.page.viewModel

import androidx.lifecycle.ViewModel
import com.iwon.githubuser.db.entity.UserEntity
import com.iwon.githubuser.db.repository.UserRepository

class ListUserViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getListUser() = userRepository.getListUser()

    fun setBookmark(userEntity: UserEntity) = userRepository.setBookmark(userEntity, true)

    fun unBookmark(userEntity: UserEntity) = userRepository.setBookmark(userEntity, false)

    fun getBookmark() = userRepository.getBookmark()
}