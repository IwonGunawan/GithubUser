package com.iwon.githubuser.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.iwon.githubuser.db.repository.UserRepository
import com.iwon.githubuser.page.viewModel.ListUserViewModel

class ViewModelFactory private constructor(private val userRepository: UserRepository) : ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun<T : ViewModel> create(modelClass: Class<T>) : T{
        if (modelClass.isAssignableFrom(ListUserViewModel::class.java)){
            return ListUserViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown view model class : ${modelClass.name}")
    }

    companion object{
        @Volatile
        private var instance : ViewModelFactory? = null
        fun getInstance(context: Context) : ViewModelFactory =
            instance ?: synchronized(this){
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }


}