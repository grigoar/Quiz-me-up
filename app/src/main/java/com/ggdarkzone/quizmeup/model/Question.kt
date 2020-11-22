package com.ggdarkzone.quizmeup.model

import com.google.gson.annotations.SerializedName

class Question {
    val category: String? = null
    val type: String? = null

    @SerializedName("question")
    val questionText: String? = null
    val difficulty: String? = null
    val correct_answer: String? = null
    val incorrect_answers: List<String>? = null

}