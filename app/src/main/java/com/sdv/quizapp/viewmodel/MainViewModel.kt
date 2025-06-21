package com.sdv.quizapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sdv.quizapp.data.QuizRepository
import com.sdv.quizapp.model.Category
import kotlinx.coroutines.launch

class MainViewModel(private val repository: QuizRepository) : ViewModel() {

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    init {
        loadCategories()
    }

    private fun loadCategories() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val categoriesList = repository.getCategories()
                _categories.value = categoriesList
                if (categoriesList.isEmpty()) {
                    _error.value = "Impossible de charger les catégories"
                }
            } catch (e: Exception) {
                _error.value = "Erreur: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun retryLoadCategories() {
        loadCategories()
    }
}