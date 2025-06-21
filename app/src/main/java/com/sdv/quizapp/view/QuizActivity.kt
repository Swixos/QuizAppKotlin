package com.sdv.quizapp.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.sdv.quizapp.R
import com.sdv.quizapp.data.QuizRepository
import com.sdv.quizapp.data.local.AppDatabase
import com.sdv.quizapp.databinding.ActivityQuizBinding
import com.sdv.quizapp.model.Question
import com.sdv.quizapp.model.User
import com.sdv.quizapp.utils.HtmlUtils
import com.sdv.quizapp.utils.ViewModelFactory
import com.sdv.quizapp.viewmodel.QuizViewModel

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private lateinit var repository: QuizRepository

    private val viewModel: QuizViewModel by viewModels {
        ViewModelFactory(repository)
    }

    private var categoryId: Int = 9 // Default: General Knowledge
    private var categoryName: String = "Culture Générale"
    private var difficulty: String = "mixed"
    private var userId: Int = 0
    private var username: String = ""

    private lateinit var answerCards: List<MaterialCardView>
    private lateinit var answerTexts: List<TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Récupérer les données de l'intent
        categoryId = intent.getIntExtra("CATEGORY_ID", 9)
        categoryName = intent.getStringExtra("CATEGORY_NAME") ?: "Culture Générale"
        difficulty = intent.getStringExtra("DIFFICULTY") ?: "mixed"
        userId = intent.getIntExtra("USER_ID", 0)
        username = intent.getStringExtra("USERNAME") ?: ""

        // Initialiser le repository
        val database = AppDatabase.getDatabase(this)
        repository = QuizRepository(database)

        setupUI()
        observeViewModel()

        // Charger les questions avec la difficulté choisie
        viewModel.loadQuestions(categoryId, difficulty)
    }

    private fun setupUI() {
        answerCards = listOf(
            binding.cardAnswer1,
            binding.cardAnswer2,
            binding.cardAnswer3,
            binding.cardAnswer4
        )

        answerTexts = listOf(
            binding.tvAnswer1,
            binding.tvAnswer2,
            binding.tvAnswer3,
            binding.tvAnswer4
        )

        // Setup click listeners for answer cards
        answerCards.forEachIndexed { index, card ->
            card.setOnClickListener {
                selectAnswer(index)
            }
        }

        binding.btnNext.setOnClickListener {
            handleNextButton()
        }
    }

    private fun observeViewModel() {
        viewModel.questions.observe(this) { questions ->
            if (questions.isNotEmpty()) {
                displayCurrentQuestion()
            }
        }

        viewModel.currentQuestionIndex.observe(this) { _ ->
            displayCurrentQuestion()
            updateProgress()
        }

        viewModel.correctAnswers.observe(this) { correct ->
            binding.tvCorrectAnswers.text = "Correctes: $correct"
        }

        viewModel.incorrectAnswers.observe(this) { incorrect ->
            binding.tvIncorrectAnswers.text = "Incorrectes: $incorrect"
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { error ->
            if (error.isNotEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.quizFinished.observe(this) { finished ->
            if (finished) {
                finishQuiz()
            }
        }

        viewModel.selectedAnswer.observe(this) { selectedAnswer ->
            binding.btnNext.isEnabled = selectedAnswer != null
        }

        viewModel.isAnswerCorrect.observe(this) { isCorrect ->
            isCorrect?.let { showAnswerResult(it) }
        }
    }

    private fun displayCurrentQuestion() {
        val question = viewModel.getCurrentQuestion() ?: return

        binding.apply {
            tvQuestion.text = HtmlUtils.decodeHtml(question.question)
            tvQuestionType.text = "Type: ${question.type.replaceFirstChar { it.uppercase() }}"
            tvDifficulty.text = "Difficulté: ${question.difficulty.replaceFirstChar { it.uppercase() }}"
            tvCategory.text = "Catégorie: ${HtmlUtils.decodeHtml(question.category)}"
        }

        // Afficher les réponses
        val answers = question.getAllAnswers()
        answerTexts.forEachIndexed { index, textView ->
            if (index < answers.size) {
                textView.text = HtmlUtils.decodeHtml(answers[index])
                answerCards[index].visibility = View.VISIBLE
            } else {
                answerCards[index].visibility = View.GONE
            }
        }

        // Reset UI state pour nouvelle question
        resetAnswerCards()
        enableAnswerCards()
        binding.btnNext.isEnabled = false
        binding.btnNext.text = if (viewModel.currentQuestionIndex.value == (viewModel.questions.value?.size ?: 1) - 1) {
            "Terminer le quiz"
        } else {
            "Question suivante"
        }
    }

    private fun selectAnswer(answerIndex: Int) {
        val question = viewModel.getCurrentQuestion() ?: return
        val answers = question.getAllAnswers()

        if (answerIndex < answers.size) {
            resetAnswerCards()

            // Mettre en surbrillance la réponse sélectionnée
            answerCards[answerIndex].setCardBackgroundColor(
                ContextCompat.getColor(this, R.color.material_blue_500)
            )
            answerTexts[answerIndex].setTextColor(
                ContextCompat.getColor(this, android.R.color.white)
            )

            viewModel.selectAnswer(answers[answerIndex])
        }
    }

    private fun resetAnswerCards() {
        answerCards.forEach { card ->
            card.setCardBackgroundColor(
                ContextCompat.getColor(this, android.R.color.white)
            )
        }
        answerTexts.forEach { textView ->
            textView.setTextColor(
                ContextCompat.getColor(this, android.R.color.black)
            )
        }
    }

    private fun enableAnswerCards() {
        answerCards.forEach { card ->
            card.isClickable = true
            card.isEnabled = true
        }
    }

    private fun disableAnswerCards() {
        answerCards.forEach { card ->
            card.isClickable = false
            card.isEnabled = false
        }
    }

    private fun showAnswerResult(isCorrect: Boolean) {
        val question = viewModel.getCurrentQuestion() ?: return
        val answers = question.getAllAnswers()
        val correctAnswerIndex = answers.indexOf(question.correctAnswer)

        // Montrer la bonne réponse en vert
        if (correctAnswerIndex >= 0) {
            answerCards[correctAnswerIndex].setCardBackgroundColor(
                ContextCompat.getColor(this, android.R.color.holo_green_light)
            )
            answerTexts[correctAnswerIndex].setTextColor(
                ContextCompat.getColor(this, android.R.color.white)
            )
        }

        // Si la réponse est incorrecte, montrer la réponse sélectionnée en rouge
        if (!isCorrect) {
            val selectedAnswer = viewModel.selectedAnswer.value
            val selectedIndex = answers.indexOf(selectedAnswer)
            if (selectedIndex >= 0 && selectedIndex != correctAnswerIndex) {
                answerCards[selectedIndex].setCardBackgroundColor(
                    ContextCompat.getColor(this, android.R.color.holo_red_light)
                )
                answerTexts[selectedIndex].setTextColor(
                    ContextCompat.getColor(this, android.R.color.white)
                )
            }
        }

        disableAnswerCards()
    }

    private fun handleNextButton() {
        // Si aucune réponse n'a été soumise, soumettre d'abord
        if (viewModel.isAnswerCorrect.value == null) {
            viewModel.submitAnswer()

            binding.btnNext.text = if (viewModel.currentQuestionIndex.value == (viewModel.questions.value?.size ?: 1) - 1) {
                "Voir les résultats"
            } else {
                "Question suivante"
            }
        } else {
            // Passer à la question suivante
            viewModel.nextQuestion()
        }
    }

    private fun updateProgress() {
        val progress = (viewModel.getProgress() * 100).toInt()
        binding.progressBarQuiz.progress = progress

        val current = (viewModel.currentQuestionIndex.value ?: 0) + 1
        val total = viewModel.questions.value?.size ?: 0
        binding.tvProgress.text = "Question $current/$total"
    }

    private fun finishQuiz() {
        val user = User(id = userId, username = username, password = "")
        viewModel.saveScore(user, categoryName)

        val intent = Intent(this, LeaderboardActivity::class.java)
        startActivity(intent)
        finish()
    }

    @Deprecated("This method has been deprecated in favor of using the OnBackPressedDispatcher")
    override fun onBackPressed() {
        super.onBackPressed()
    }
}