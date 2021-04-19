package io.github.theriverelder.customqna.data.serialization

import io.github.theriverelder.customqna.data.QnaSet
import io.github.theriverelder.customqna.data.QnaSetInfo
import io.github.theriverelder.customqna.data.UserProgress
import io.github.theriverelder.customqna.data.UserProgressInfo
import java.io.DataInput
import java.io.DataOutput

interface DataSerialization {
    val version: Int

    fun readQnaSet(input: DataInput): QnaSet?
    fun writeQnaSet(output: DataOutput, qnaSet: QnaSet): Boolean

    fun readUserProgress(input: DataInput): UserProgress?
    fun writeUserProgress(output: DataOutput, userProgress: UserProgress): Boolean

    fun readQnaSetInfo(input: DataInput): QnaSetInfo?
    fun readUserProgressInfo(input: DataInput): UserProgressInfo?
}