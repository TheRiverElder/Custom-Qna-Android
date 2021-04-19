package io.github.theriverelder.customqna.data

data class QnaItem(
    val qiuid: Int,
    var question: String,
    var answer: String,
    var hint: String
)