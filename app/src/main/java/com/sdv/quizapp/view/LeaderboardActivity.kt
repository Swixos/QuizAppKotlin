package com.sdv.quizapp.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.sdv.quizapp.data.QuizRepository
import com.sdv.quizapp.data.local.AppDatabase
import com.sdv.quizapp.databinding.ActivityLeaderboardBinding
import com.sdv.quizapp.model.Score
import com.sdv.quizapp.utils.ViewModelFactory
import com.sdv.quizapp.viewmodel.LeaderboardViewModel

class LeaderboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLeaderboardBinding
    private lateinit var repository: QuizRepository
    private lateinit var scoreAdapter: ScoreAdapter

    private val viewModel: LeaderboardViewModel by viewModels {
        ViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialiser le repository
        val database = AppDatabase.getDatabase(this)
        repository = QuizRepository(database)

        setupUI()
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupUI() {
        binding.btnBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun setupRecyclerView() {
        scoreAdapter = ScoreAdapter()
        binding.recyclerViewScores.apply {
            adapter = scoreAdapter
            layoutManager = LinearLayoutManager(this@LeaderboardActivity)
        }
    }

    private fun observeViewModel() {
        viewModel.scores.observe(this) { scores ->
            updateUI(scores)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { error ->
            if (error.isNotEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateUI(scores: List<Score>) {
        val topThree = viewModel.getTopThreeScores()
        val remaining = viewModel.getRemainingScores()

        // Mettre à jour le podium
        updatePodium(topThree)

        // Mettre à jour la liste des autres scores
        scoreAdapter.submitList(remaining)

        // Masquer la section "Autres classements" s'il n'y a pas de scores supplémentaires
        if (remaining.isEmpty()) {
            binding.tvOtherRankings.visibility = View.GONE
            binding.recyclerViewScores.visibility = View.GONE
        } else {
            binding.tvOtherRankings.visibility = View.VISIBLE
            binding.recyclerViewScores.visibility = View.VISIBLE
        }
    }

    private fun updatePodium(topThree: List<Score>) {
        binding.apply {
            // 1ère place
            if (topThree.isNotEmpty()) {
                tvFirstPlace.text = topThree[0].username
                tvFirstScore.text = "${topThree[0].score}/10"
                tvFirstPlace.visibility = View.VISIBLE
                tvFirstScore.visibility = View.VISIBLE
            } else {
                tvFirstPlace.text = "N/A"
                tvFirstScore.text = "0/10"
            }

            // 2ème place
            if (topThree.size > 1) {
                tvSecondPlace.text = topThree[1].username
                tvSecondScore.text = "${topThree[1].score}/10"
                tvSecondPlace.visibility = View.VISIBLE
                tvSecondScore.visibility = View.VISIBLE
            } else {
                tvSecondPlace.text = "N/A"
                tvSecondScore.text = "0/10"
            }

            // 3ème place
            if (topThree.size > 2) {
                tvThirdPlace.text = topThree[2].username
                tvThirdScore.text = "${topThree[2].score}/10"
                tvThirdPlace.visibility = View.VISIBLE
                tvThirdScore.visibility = View.VISIBLE
            } else {
                tvThirdPlace.text = "N/A"
                tvThirdScore.text = "0/10"
            }
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
        super.onBackPressed()
    }
}