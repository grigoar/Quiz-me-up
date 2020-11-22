package com.ggdarkzone.quizmeup.controller

import com.ggdarkzone.quizmeup.model.QuizApiResponse
import com.ggdarkzone.quizmeup.model.TokenApiResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface QuizJsonApi {
    @GET("api.php")
    fun getAllQuestions(@Query("amount")amount: Int?=1,@Query("category") category:Int?=null,
                        @Query("difficulty") difficulty:String?=null,@Query("type")type:String?=null,
                        @Query("token") token: String? = null,
                        @Query("encode") encodeFormat: String? = "base64"): Call<QuizApiResponse?>?

    @GET("api_token.php")
    fun getToken(@Query("command")command: String? = "request"): Call<TokenApiResponse>

    @GET("api_token.php")
    fun getResetToken(@Query("command") command: String? = "reset", @Query("token")token: String?): Call<TokenApiResponse>

}