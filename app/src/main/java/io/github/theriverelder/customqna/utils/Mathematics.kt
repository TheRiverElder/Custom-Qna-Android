package io.github.theriverelder.customqna.utils

fun <T> randomItems(list: List<T>, count: Int): List<T> {
    val copy = ArrayList(list)
    val actualCount = count.coerceAtLeast(0).coerceAtMost(list.size)
    for (i in 0 until actualCount) {
        val j: Int = (Math.random() * list.size).toInt()
        if (i == j) continue
        val tmp = copy[i]
        copy[i] = copy[j]
        copy[j] = tmp
    }
    return copy.subList(0, actualCount)
}