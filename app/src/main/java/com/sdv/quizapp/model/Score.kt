package com.sdv.quizapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scores")
data class Score(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val username: String,
    val score: Int,
    val category: String,
    val timestamp: Long = System.currentTimeMillis()
)