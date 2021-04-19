package io.github.theriverelder.customqna.data

import java.util.*

data class UserProgressInfo(
    val upuid: Long,
    val qnaSetInfo: QnaSetInfo?,
    val completedItemCount: Int,
    var dailyTaskDate: Date,
    val dailyTaskItemCount: Int
)