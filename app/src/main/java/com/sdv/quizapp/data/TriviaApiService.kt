package com.sdv.quizapp.data.remote

import com.sdv.quizapp.model.Category
import com.sdv.quizapp.model.Question
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class TriviaResponse(
    val response_code: Int,
    val results: List<Question>
)

data class CategoryResponse(
    val trivia_categories: List<Category>
)

interface TriviaApiService {

    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") amount: Int = 10,
        @Query("category") category: Int? = null,
        @Query("difficulty") difficulty: String? = null,
        @Query("type") type: String = "multiple"
    ): Response<TriviaResponse>

    @GET("api_category.php")
    suspend fun getCategories(): Response<CategoryResponse>

    companion object {
        private const val BASE_URL = "https://opentdb.com/"

        fun create(): TriviaApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TriviaApiService::class.java)
        }
    }
}