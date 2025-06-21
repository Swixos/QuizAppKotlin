package com.sdv.quizapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sdv.quizapp.data.QuizRepository
import com.sdv.quizapp.model.Question
import com.sdv.quizapp.model.Score
import com.sdv.quizapp.model.User
import kotlinx.coroutines.launch

class QuizViewModel(private val repository: QuizRepository) : ViewModel() {

    private val _questions = MutableLiveData<List<Question>>()
    val questions: LiveData<List<Question>> = _questions

    private val _currentQuestionIndex = MutableLiveData<Int>(0)
    val currentQuestionIndex: LiveData<Int> = _currentQuestionIndex

    private val _correctAnswers = MutableLiveData<Int>(0)
    val correctAnswers: LiveData<Int> = _correctAnswers

    private val _incorrectAnswers = MutableLiveData<Int>(0)
    val incorrectAnswers: LiveData<Int> = _incorrectAnswers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _quizFinished = MutableLiveData<Boolean>()
    val quizFinished: LiveData<Boolean> = _quizFinished

    private val _selectedAnswer = MutableLiveData<String?>()
    val selectedAnswer: LiveData<String?> = _selectedAnswer

    // ✅ CORRECTION : Utiliser Boolean? au lieu de Boolean
    private val _isAnswerCorrect = MutableLiveData<Boolean?>()
    val isAnswerCorrect: LiveData<Boolean?> = _isAnswerCorrect

    fun loadQuestions(categoryId: Int, difficulty: String = "mixed") {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val questionsList = repository.getQuestions(categoryId, difficulty)
                if (questionsList.isNotEmpty()) {
                    _questions.value = questionsList
                    _currentQuestionIndex.value = 0
                    _correctAnswers.value = 0
                    _incorrectAnswers.value = 0
                    // ✅ AJOUT : Réinitialiser l'état pour la première question
                    resetQuestionState()
                } else {
                    _error.value = "Impossible de charger les questions pour cette difficulté"
                }
            } catch (e: Exception) {
                _error.value = "Erreur: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectAnswer(answer: String) {
        _selectedAnswer.value = answer
    }

    fun submitAnswer() {
        val currentQuestion = getCurrentQuestion()
        val selected = _selectedAnswer.value

        if (currentQuestion != null && selected != null) {
            val isCorrect = selected == currentQuestion.correctAnswer
            _isAnswerCorrect.value = isCorrect

            if (isCorrect) {
                _correctAnswers.value = (_correctAnswers.value ?: 0) + 1
            } else {
                _incorrectAnswers.value = (_incorrectAnswers.value ?: 0) + 1
            }
        }
    }

    fun nextQuestion() {
        val currentIndex = _currentQuestionIndex.value ?: 0
        val totalQuestions = _questions.value?.size ?: 0

        if (currentIndex + 1 < totalQuestions) {
            _currentQuestionIndex.value = currentIndex + 1
            // ✅ CORRECTION : Réinitialiser complètement l'état pour la nouvelle question
            resetQuestionState()
        } else {
            _quizFinished.value = true
        }
    }

    // ✅ AJOUT : Nouvelle méthode pour réinitialiser l'état d'une question
    private fun resetQuestionState() {
        _selectedAnswer.value = null
        _isAnswerCorrect.value = null
    }

    fun getCurrentQuestion(): Question? {
        val questions = _questions.value
        val index = _currentQuestionIndex.value ?: 0
        return if (questions != null && index < questions.size) {
            questions[index]
        } else null
    }

    fun getProgress(): Float {
        val current = (_currentQuestionIndex.value ?: 0) + 1
        val total = _questions.value?.size ?: 1
        return current.toFloat() / total.toFloat()
    }

    fun saveScore(user: User, categoryName: String) {
        val score = _correctAnswers.value ?: 0
        viewModelScope.launch {
            try {
                val scoreEntity = Score(
                    userId = user.id,
                    username = user.username,
                    score = score,
                    category = categoryName
                )
                repository.insertScore(scoreEntity)
            } catch (e: Exception) {
                _error.value = "Erreur lors de la sauvegarde: ${e.message}"
            }
        }
    }
}