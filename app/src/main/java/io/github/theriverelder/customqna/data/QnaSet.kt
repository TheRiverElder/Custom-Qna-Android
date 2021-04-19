package io.github.theriverelder.customqna.data

import io.github.theriverelder.customqna.utils.OrderedMap

class QnaSet(
    val qsuid: Long,
    var version: String,
    var name: String,
    var description: String,
    var itemCounter: Int,
    val items: OrderedMap<QnaItem, Int>
) {
    fun extractInfo() = QnaSetInfo(
        qsuid,
        version,
        name,
        description,
        items.size
    )

    fun createNextItem(): QnaItem = QnaItem(itemCounter++, "", "", "")
}