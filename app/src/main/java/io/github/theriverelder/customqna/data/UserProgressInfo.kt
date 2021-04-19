package io.github.theriverelder.customqna.data

import java.util.*

data class UserProgressInfo(
    val upuid: Long,
    val qsuid: Long,
    var dailyTaskDate: Date,
    val dailyTaskItemCount: Int,
    val completedItemCount: Int
)