package com.sdv.quizapp.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sdv.quizapp.data.QuizRepository
import com.sdv.quizapp.viewmodel.LoginViewModel
import com.sdv.quizapp.viewmodel.MainViewModel
import com.sdv.quizapp.viewmodel.QuizViewModel
import com.sdv.quizapp.viewmodel.LeaderboardViewModel

class ViewModelFactory(private val repository: QuizRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            LoginViewModel::class.java -> LoginViewModel(repository) as T
            MainViewModel::class.java -> MainViewModel(repository) as T
            QuizViewModel::class.java -> QuizViewModel(repository) as T
            LeaderboardViewModel::class.java -> LeaderboardViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}