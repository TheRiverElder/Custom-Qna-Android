package io.github.theriverelder.customqna.data.serialization

import io.github.theriverelder.customqna.data.*
import io.github.theriverelder.customqna.utils.OrderedMap
import java.io.DataInput
import java.io.DataOutput

object DataSerializationVersion_1 : DataSerialization {
    override val version: Int = 1

    override fun readQnaSet(input: DataInput): QnaSet? {
        if (input.readInt() != 1) return null

        val qsuid: Long = input.readLong()
        val version: String = input.readUTF()
        val name: String = input.readUTF()
        val description: String = input.readUTF()
        val itemCounter: Int = input.readInt()
        val items: OrderedMap<QnaItem, Int> = OrderedMap { it.qiuid }

        val itemCount = input.readInt()
        for (i in 0 until itemCount) {
            items.put(QnaItem(
                input.readInt(),
                input.readUTF(),
                input.readUTF(),
                input.readUTF()
            ))
        }

        return QnaSet(qsuid, version, name, description, itemCounter, items)
    }

    override fun writeQnaSet(output: DataOutput, qnaSet: QnaSet): Boolean {
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
    }

    override fun readUserProgress(input: DataInput): UserProgress? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun writeUserProgress(output: DataOutput, userProgress: UserProgress): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun readQnaSetInfo(input: DataInput): QnaSetInfo? {
        if (input.readInt() != 1) return null

        val qsuid: Long = input.readLong()
        val version: String = input.readUTF()
        val name: String = input.readUTF()
        val description: String = input.readUTF()
        input.readInt() // itemCounter
        val itemsCount: Int = input.readInt()

        return QnaSetInfo(qsuid, version, name, description, itemsCount)
    }

    override fun readUserProgressInfo(input: DataInput): UserProgressInfo? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}