package io.github.theriverelder.customqna.data.serialization

import io.github.theriverelder.customqna.data.*
import io.github.theriverelder.customqna.utils.OrderedMap
import java.io.DataInput
import java.io.DataOutput
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

object DataSerializationVersion1 : DataSerialization {

    override val version: Int = 1

    override fun readQnaSet(input: DataInput): QnaSet? {
        try {
            if (input.readInt() != 1) return null

            val qsuid: Long = input.readLong()
            val version: String = input.readUTF()
            val name: String = input.readUTF()
            val description: String = input.readUTF()
            val itemCounter: Int = input.readInt()
            val items: OrderedMap<QnaItem, Int> = OrderedMap { it.qiuid }

            val itemCount = input.readInt()
            for (i in 0 until itemCount) {
                items.put(
                    QnaItem(
                        input.readInt(),
                        input.readUTF(),
                        input.readUTF(),
                        input.readUTF()
                    )
                )
            }

            return QnaSet(qsuid, version, name, description, itemCounter, items)
        } catch (e: Exception) {
            return null
        }
    }

    override fun writeQnaSet(output: DataOutput, qnaSet: QnaSet): Boolean {
        try {
            output.writeInt(1)

            output.writeLong(qnaSet.qsuid)
            output.writeUTF(qnaSet.version)
            output.writeUTF(qnaSet.name)
            output.writeUTF(qnaSet.description)
            output.writeInt(qnaSet.itemCounter)

            output.writeInt(qnaSet.items.size)
            for (item in qnaSet.items.toList()) {
                output.writeInt(item.qiuid)
                output.writeUTF(item.question)
                output.writeUTF(item.answer)
                output.writeUTF(item.hint)
            }
            return true
        } catch (e: Exception) {
            return false
        }
    }

    override fun readUserProgress(input: DataInput): UserProgress? {
        try {
            if (input.readInt() != 1) return null

            val upuid: Long = input.readLong()
            val qsuid: Long = input.readLong()
            val dailyTaskDate: Date = Date(input.readLong())
            val dailyTaskQiuidCount: Int = input.readInt()
            val completedQuidCount: Int = input.readInt()

            val dailyTaskQiuidListTmp = ArrayList<Int>(dailyTaskQiuidCount)
            for (i in 0 until dailyTaskQiuidCount) {
                dailyTaskQiuidListTmp.add(input.readInt())
            }
            val dailyTaskQiuidList: List<Int> = dailyTaskQiuidListTmp

            val completedQiuidSet: MutableSet<Int> = HashSet()
            for (i in 0 until completedQuidCount) {
                completedQiuidSet.add(input.readInt())
            }

            return UserProgress(upuid, qsuid, dailyTaskDate, dailyTaskQiuidList, completedQiuidSet)
        } catch (e: Exception) {
            return null
        }
    }

    override fun writeUserProgress(output: DataOutput, userProgress: UserProgress): Boolean {
        return try {
            output.writeInt(1)

            output.writeLong(userProgress.upuid)
            output.writeLong(userProgress.qsuid)
            output.writeLong(userProgress.dailyTaskDate.time)
            output.writeInt(userProgress.dailyTaskQiuidList.size)
            output.writeInt(userProgress.completedQiuidSet.size)
            userProgress.dailyTaskQiuidList.forEach { output.writeInt(it) }
            userProgress.completedQiuidSet.forEach { output.writeInt(it) }

            true
        } catch (e: Exception) {
            false
        }
    }

    override fun readQnaSetInfo(input: DataInput): QnaSetInfo? {
        try {
            if (input.readInt() != 1) return null

            val qsuid: Long = input.readLong()
            val version: String = input.readUTF()
            val name: String = input.readUTF()
            val description: String = input.readUTF()
            input.readInt() // itemCounter
            val itemsCount: Int = input.readInt()

            return QnaSetInfo(qsuid, version, name, description, itemsCount)
        } catch (e: Exception) {
            return null
        }
    }

    override fun readUserProgressInfo(input: DataInput): UserProgressInfo? {
        try {
            if (input.readInt() != 1) return null

            val upuid: Long = input.readLong()
            val qsuid: Long = input.readLong()
            val dailyTaskDate: Date = Date(input.readLong())
            val dailyTaskQiuidCount: Int = input.readInt()
            val completedQuidCount: Int = input.readInt()

            return UserProgressInfo(
                upuid,
                qsuid,
                dailyTaskDate,
                dailyTaskQiuidCount,
                completedQuidCount
            )
        } catch (e: Exception) {
            return null
        }
    }

}