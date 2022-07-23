package com.iwon.githubuser.page.viewModel

import androidx.lifecycle.ViewModel
import com.iwon.githubuser.db.repository.UserRepository

class ListUserViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getListUser() = userRepository.getListUser()
}