package com.sdv.quizapp.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sdv.quizapp.databinding.ActivityDifficultySelectionBinding

class DifficultySelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDifficultySelectionBinding

    private var categoryId: Int = 0
    private var categoryName: String = ""
    private var userId: Int = 0
    private var username: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDifficultySelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Récupérer les données de l'intent
        categoryId = intent.getIntExtra("CATEGORY_ID", 0)
        categoryName = intent.getStringExtra("CATEGORY_NAME") ?: ""
        userId = intent.getIntExtra("USER_ID", 0)
        username = intent.getStringExtra("USERNAME") ?: ""

        setupUI()
        setupClickListeners()
    }

    private fun setupUI() {
        binding.tvSelectedCategory.text = "Catégorie: $categoryName"
    }

    private fun setupClickListeners() {
        // Difficulté Facile
        binding.cardEasy.setOnClickListener {
            startQuiz("easy")
        }

        // Difficulté Moyenne
        binding.cardMedium.setOnClickListener {
            startQuiz("medium")
        }

        // Difficulté Difficile
        binding.cardHard.setOnClickListener {
            startQuiz("hard")
        }

        // Mixte (toutes difficultés)
        binding.cardMixed.setOnClickListener {
            startQuiz("mixed")
        }

        // Bouton retour
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun startQuiz(difficulty: String) {
        val intent = Intent(this, QuizActivity::class.java).apply {
            putExtra("CATEGORY_ID", categoryId)
            putExtra("CATEGORY_NAME", categoryName)
            putExtra("DIFFICULTY", difficulty)
            putExtra("USER_ID", userId)
            putExtra("USERNAME", username)
        }
        startActivity(intent)
    }
}