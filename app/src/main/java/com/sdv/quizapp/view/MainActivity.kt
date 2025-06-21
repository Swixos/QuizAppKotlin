package com.sdv.quizapp.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.sdv.quizapp.R
import com.sdv.quizapp.data.QuizRepository
import com.sdv.quizapp.data.local.AppDatabase
import com.sdv.quizapp.databinding.ActivityMainBinding
import com.sdv.quizapp.model.Category
import com.sdv.quizapp.utils.ViewModelFactory
import com.sdv.quizapp.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var repository: QuizRepository

    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory(repository)
    }

    private var selectedCategory: Category? = null
    private var userId: Int = 0
    private var username: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Récupérer les données utilisateur
        userId = intent.getIntExtra("USER_ID", 0)
        username = intent.getStringExtra("USERNAME") ?: ""

        // Initialiser le repository
        val database = AppDatabase.getDatabase(this)
        repository = QuizRepository(database)

        setupUI()
        observeViewModel()
    }

    private fun setupUI() {
        binding.apply {
            tvWelcome.text = "Bienvenue $username !"

            btnStartQuiz.setOnClickListener {
                selectedCategory?.let { category ->
                    showConfirmationDialog(category)
                }
            }

            btnLeaderboard.setOnClickListener {
                startActivity(Intent(this@MainActivity, LeaderboardActivity::class.java))
            }
        }
    }

    private fun observeViewModel() {
        viewModel.categories.observe(this) { categories ->
            setupCategorySpinner(categories)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.cardCategorySelection.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        viewModel.error.observe(this) { error ->
            if (error.isNotEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupCategorySpinner(categories: List<Category>) {
        val categoryNames = categories.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categoryNames)

        binding.spinnerCategories.setAdapter(adapter)
        binding.spinnerCategories.setOnItemClickListener { _, _, position, _ ->
            selectedCategory = categories[position]
            binding.btnStartQuiz.isEnabled = true
        }
    }

    private fun showConfirmationDialog(category: Category) {
        AlertDialog.Builder(this)
            .setTitle("Confirmation")
            .setMessage("Voulez-vous commencer le quiz dans la catégorie \"${category.name}\" ?\n\nVous aurez 10 questions à répondre.")
            .setPositiveButton("Commencer") { _, _ ->
                startQuiz(category)
            }
            .setNegativeButton("Annuler", null)
            .show()
    }

    private fun startQuiz(category: Category) {
        val intent = Intent(this, QuizActivity::class.java).apply {
            putExtra("CATEGORY_ID", category.id)
            putExtra("CATEGORY_NAME", category.name)
            putExtra("USER_ID", userId)
            putExtra("USERNAME", username)
        }
        startActivity(intent)
    }
}