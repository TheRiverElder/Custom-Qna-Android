package io.github.theriverelder.customqna.data

import java.util.*

data class UserProgress(
    val upuid: Long,
    val qsuid: Long,
    var dailyTaskDate: Date,
    var dailyTaskQiuidList: List<Int>,
    val completedQiuidSet: MutableSet<Int>
) {
    fun extractInfo() = UserProgressInfo(
        upuid,
        qsuid,
        dailyTaskDate,
        dailyTaskQiuidList.size,
        completedQiuidSet.size
    )
}