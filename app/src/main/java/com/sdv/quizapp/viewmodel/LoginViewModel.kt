package com.sdv.quizapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sdv.quizapp.data.QuizRepository
import com.sdv.quizapp.model.User
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: QuizRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _loginResult.value = LoginResult.Error("Veuillez remplir tous les champs")
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val user = repository.login(username, password)
                if (user != null) {
                    _loginResult.value = LoginResult.Success(user)
                } else {
                    _loginResult.value = LoginResult.Error("Nom d'utilisateur ou mot de passe incorrect")
                }
            } catch (e: Exception) {
                _loginResult.value = LoginResult.Error("Erreur de connexion: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun register(username: String, password: String, confirmPassword: String) {
        if (username.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            _loginResult.value = LoginResult.Error("Veuillez remplir tous les champs")
            return
        }

        if (password != confirmPassword) {
            _loginResult.value = LoginResult.Error("Les mots de passe ne correspondent pas")
            return
        }

        if (password.length < 4) {
            _loginResult.value = LoginResult.Error("Le mot de passe doit contenir au moins 4 caractères")
            return
        }

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val existingUser = repository.getUserByUsername(username)
                if (existingUser != null) {
                    _loginResult.value = LoginResult.Error("Ce nom d'utilisateur existe déjà")
                } else {
                    val user = User(username = username, password = password)
                    val userId = repository.registerUser(user)
                    _loginResult.value = LoginResult.Success(user.copy(id = userId.toInt()))
                }
            } catch (e: Exception) {
                _loginResult.value = LoginResult.Error("Erreur d'inscription: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

    sealed class LoginResult {
        data class Success(val user: User) : LoginResult()
        data class Error(val message: String) : LoginResult()
    }
}