package io.github.theriverelder.customqna

import android.content.Context
import io.github.theriverelder.customqna.data.QnaSet
import io.github.theriverelder.customqna.data.QnaSetInfo
import io.github.theriverelder.customqna.data.UserProgress
import io.github.theriverelder.customqna.data.UserProgressInfo
import io.github.theriverelder.customqna.data.serialization.DataSerialization
import io.github.theriverelder.customqna.data.serialization.DataSerializationVersion1
import io.github.theriverelder.customqna.utils.OrderedMap
import io.github.theriverelder.customqna.utils.toUidString
import java.io.*
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

object Manifest {
    var qnaSetInfoMap: MutableMap<Long, QnaSetInfo> = HashMap()
    var userProgressInfoMap: MutableMap<Long, UserProgressInfo> = HashMap()
}

var qnaSetDir: File = File(".")
var userProgressDir: File = File(".")
val serialization: DataSerialization = DataSerializationVersion1

fun setup(context: Context) {
    qnaSetDir = File(context.filesDir, "qna_sets")
    userProgressDir = File(context.filesDir, "user_progresses")

    if (!qnaSetDir.exists()) {
        qnaSetDir.mkdir()
    }
    if (!userProgressDir.exists()) {
        userProgressDir.mkdir()
    }


    Manifest.qnaSetInfoMap.clear()
    qnaSetDir.listFiles()?.forEach {
        val input: DataInput = DataInputStream(FileInputStream(it))
        val r = serialization.readQnaSetInfo(input)
        if (r != null) {
            Manifest.qnaSetInfoMap[r.qsuid] = r
        }
    }

    Manifest.userProgressInfoMap.clear()
    userProgressDir.listFiles()?.forEach {
        val input: DataInput = DataInputStream(FileInputStream(it))
        val r = serialization.readUserProgressInfo(input)
        if (r != null) {
            Manifest.userProgressInfoMap[r.upuid] = r
        }
    }
}

fun readQnaSet(qsuid: Long): QnaSet? {
    val fileName = qsuid.toUidString()
    val file = File(qnaSetDir, fileName)
    if (!file.exists()) return null
    val input = DataInputStream(FileInputStream(file))
    val result = serialization.readQnaSet(input)
    input.close()
    if (result != null) {
        Manifest.qnaSetInfoMap[result.qsuid] = result.extractInfo()
    }
    return result
}

fun writeQnaSet(qnaSet: QnaSet): Boolean {
    val fileName = qnaSet.qsuid.toUidString()
    val file = File(qnaSetDir, fileName)
    if (file.exists() && !file.isFile) return false
    val output = DataOutputStream(FileOutputStream(file))
    val result = serialization.writeQnaSet(output, qnaSet)
    output.close()
    if (result) {
        Manifest.qnaSetInfoMap[qnaSet.qsuid] = qnaSet.extractInfo()
    }
    return result
}

fun readUserProgress(upuid: Long): UserProgress? {
    val fileName = upuid.toUidString()
    val file = File(userProgressDir, fileName)
    if (!file.exists()) return null
    val input = DataInputStream(FileInputStream(file))
    val result = serialization.readUserProgress(input)
    input.close()
    if (result != null) {
        Manifest.userProgressInfoMap[result.upuid] = result.extractInfo()
    }
    return result
}

fun writeUserProgress(userProgress: UserProgress): Boolean {
    val fileName = userProgress.upuid.toUidString()
    val file = File(userProgressDir, fileName)
    if (file.exists() && !file.isFile) return false
    val output = DataOutputStream(FileOutputStream(file))
    val result = serialization.writeUserProgress(output, userProgress)
    output.close()
    if (result) {
        Manifest.userProgressInfoMap[userProgress.upuid] = userProgress.extractInfo()
    }
    return result
}

fun deleteQnaSet(qsuid: Long) {
    val fileName = qsuid.toUidString()
    val file = File(qnaSetDir, fileName)
    if (file.exists()) {
        file.delete()
    }
}

fun deleteUserProgress(upuid: Long) {
    val fileName = upuid.toUidString()
    val file = File(userProgressDir, fileName)
    if (file.exists()) {
        file.delete()
    }
}

fun dispose() {

}

fun createUserProgress(qnaSet: QnaSet): UserProgress = UserProgress(
    System.currentTimeMillis(),
    qnaSet.qsuid,
    Date(0),
    Collections.emptyList(),
    HashSet()
)

fun createQnaSet(): QnaSet = QnaSet(
    System.currentTimeMillis(),
    "1.0.0",
    "Unnamed",
    "Created at ${Date()}",
    0,
    OrderedMap { it.qiuid }
)