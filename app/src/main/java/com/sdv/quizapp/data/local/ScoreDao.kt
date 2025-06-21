package com.sdv.quizapp.data.local

import androidx.room.*
import com.sdv.quizapp.model.Score

@Dao
interface ScoreDao {

    @Insert
    suspend fun insertScore(score: Score)

    @Query("SELECT * FROM scores ORDER BY score DESC")
    suspend fun getAllScoresOrderedByScore(): List<Score>

    @Query("SELECT * FROM scores WHERE userId = :userId ORDER BY score DESC")
    suspend fun getScoresByUser(userId: Int): List<Score>

    @Query("SELECT * FROM scores WHERE category = :category ORDER BY score DESC")
    suspend fun getScoresByCategory(category: String): List<Score>
}