package com.sdv.quizapp.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.sdv.quizapp.data.QuizRepository
import com.sdv.quizapp.data.local.AppDatabase
import com.sdv.quizapp.databinding.ActivityLoginBinding
import com.sdv.quizapp.model.User
import com.sdv.quizapp.utils.ViewModelFactory
import com.sdv.quizapp.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var repository: QuizRepository

    private val viewModel: LoginViewModel by viewModels {
        ViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialiser le repository
        val database = AppDatabase.getDatabase(this)
        repository = QuizRepository(database)

        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (validateInput(username, password)) {
                viewModel.login(username, password)
            }
        }

        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (validateInput(username, password)) {
                viewModel.register(username, password, password) // Confirmation = même mot de passe
            }
        }
    }

    private fun validateInput(username: String, password: String): Boolean {
        return when {
            username.isEmpty() -> {
                binding.etUsername.error = "Le nom d'utilisateur est requis"
                false
            }
            password.isEmpty() -> {
                binding.etPassword.error = "Le mot de passe est requis"
                false
            }
            password.length < 3 -> {
                binding.etPassword.error = "Le mot de passe doit contenir au moins 3 caractères"
                false
            }
            else -> true
        }
    }

    private fun observeViewModel() {
        viewModel.loginResult.observe(this) { result ->
            when (result) {
                is LoginViewModel.LoginResult.Success -> {
                    navigateToMain(result.user)
                }
                is LoginViewModel.LoginResult.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnLogin.isEnabled = !isLoading
            binding.btnRegister.isEnabled = !isLoading
        }
    }

    private fun navigateToMain(user: User) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("USER_ID", user.id.toInt())
            putExtra("USERNAME", user.username)
        }
        startActivity(intent)
        finish()
    }
}