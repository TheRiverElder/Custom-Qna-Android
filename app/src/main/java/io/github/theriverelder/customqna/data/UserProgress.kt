package io.github.theriverelder.customqna.data

import io.github.theriverelder.customqna.Manifest
import java.util.*

data class UserProgress(
    val upuid: Long,
    val qsuid: Long,
    val completedQiuidSet: MutableSet<Int>,
    var dailyTaskDate: Date,
    var dailyTaskQiuidList: List<Int>
) {
    fun extractInfo() = UserProgressInfo(
        upuid,
        Manifest.qnaSetInfoMap[qsuid],
        completedQiuidSet.size,
        dailyTaskDate,
        dailyTaskQiuidList.size
    )
}