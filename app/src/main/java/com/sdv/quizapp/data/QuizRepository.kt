package com.sdv.quizapp.data

import com.sdv.quizapp.data.local.AppDatabase
import com.sdv.quizapp.data.remote.TriviaApiService
import com.sdv.quizapp.model.Category
import com.sdv.quizapp.model.Question
import com.sdv.quizapp.model.Score
import com.sdv.quizapp.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QuizRepository(private val database: AppDatabase) {
    private val triviaApi = TriviaApiService.create()

    // Méthode mise à jour avec paramètre difficulty - API UNIQUEMENT
    suspend fun getQuestions(categoryId: Int, difficulty: String = "mixed"): List<Question> {
        return withContext(Dispatchers.IO) {
            try {
                val response = triviaApi.getQuestions(
                    amount = 10,
                    category = categoryId,
                    difficulty = if (difficulty == "mixed") null else difficulty
                )

                if (response.isSuccessful) {
                    response.body()?.results ?: emptyList()
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    suspend fun getCategories(): List<Category> {
        return withContext(Dispatchers.IO) {
            try {
                val response = triviaApi.getCategories()
                if (response.isSuccessful) {
                    response.body()?.trivia_categories ?: emptyList()
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    // Méthodes pour la base de données locale
    suspend fun insertUser(user: User): Long {
        return withContext(Dispatchers.IO) {
            database.userDao().insertUser(user)
        }
    }

    suspend fun getUserByUsername(username: String): User? {
        return withContext(Dispatchers.IO) {
            database.userDao().getUserByUsername(username)
        }
    }

    suspend fun insertScore(score: Score) {
        withContext(Dispatchers.IO) {
            database.scoreDao().insertScore(score)
        }
    }

    suspend fun getTopScores(): List<Score> {
        return withContext(Dispatchers.IO) {
            database.scoreDao().getAllScoresOrderedByScore()
        }
    }

    // Méthode pour LeaderboardViewModel
    suspend fun getAllScores(): List<Score> {
        return withContext(Dispatchers.IO) {
            database.scoreDao().getAllScoresOrderedByScore()
        }
    }

    // Méthodes pour LoginViewModel
    suspend fun login(username: String, password: String): User? {
        return withContext(Dispatchers.IO) {
            database.userDao().login(username, password)
        }
    }

    suspend fun registerUser(user: User): Long {
        return withContext(Dispatchers.IO) {
            database.userDao().insertUser(user)
        }
    }
}