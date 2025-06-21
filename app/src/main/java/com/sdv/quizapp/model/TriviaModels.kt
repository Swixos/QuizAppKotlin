package com.sdv.quizapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class TriviaResponse(
    @SerializedName("response_code")
    val responseCode: Int,
    val results: List<Question>
)

data class CategoryResponse(
    @SerializedName("trivia_categories")
    val triviaCategories: List<Category>
)

@Parcelize
data class Category(
    val id: Int,
    val name: String
) : Parcelable

@Parcelize
data class Question(
    val type: String,
    val difficulty: String,
    val category: String,
    val question: String,
    @SerializedName("correct_answer")
    val correctAnswer: String,
    @SerializedName("incorrect_answers")
    val incorrectAnswers: List<String>
) : Parcelable {
    fun getAllAnswers(): List<String> {
        return (incorrectAnswers + correctAnswer).shuffled()
    }
}