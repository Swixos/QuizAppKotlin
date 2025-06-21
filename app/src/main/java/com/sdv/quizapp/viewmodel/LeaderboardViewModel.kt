package com.sdv.quizapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sdv.quizapp.data.QuizRepository
import com.sdv.quizapp.model.Score
import kotlinx.coroutines.launch

class LeaderboardViewModel(private val repository: QuizRepository) : ViewModel() {

    private val _scores = MutableLiveData<List<Score>>()
    val scores: LiveData<List<Score>> = _scores

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        loadScores()
    }

    private fun loadScores() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val scoresList = repository.getAllScores()
                _scores.value = scoresList
            } catch (e: Exception) {
                _error.value = "Erreur: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshScores() {
        loadScores()
    }

    fun getTopThreeScores(): List<Score> {
        return _scores.value?.take(3) ?: emptyList()
    }

    fun getRemainingScores(): List<Score> {
        return _scores.value?.drop(3) ?: emptyList()
    }

    fun getScoresByCategory(category: String): List<Score> {
        return _scores.value?.filter { it.category == category } ?: emptyList()
    }

    fun getTotalPlayers(): Int {
        return _scores.value?.distinctBy { it.userId }?.size ?: 0
    }

    fun getBestScoreForUser(userId: Int): Score? {
        return _scores.value?.filter { it.userId == userId }?.maxByOrNull { it.score }
    }

    fun getAverageScore(): Double {
        val scores = _scores.value
        return if (scores.isNullOrEmpty()) {
            0.0
        } else {
            scores.map { it.score }.average()
        }
    }

    fun clearError() {
        _error.value = ""
    }
}