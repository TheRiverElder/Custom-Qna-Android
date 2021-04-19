package io.github.theriverelder.customqna

import android.graphics.Color.rgb
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.github.theriverelder.customqna.data.QnaItem
import io.github.theriverelder.customqna.data.QnaSet
import io.github.theriverelder.customqna.data.UserProgress
import io.github.theriverelder.customqna.utils.randomItems
import io.github.theriverelder.customqna.utils.toUidString
import java.util.*
import kotlin.collections.ArrayList


const val STAGE_SHOW_QUESTION = 1
const val STAGE_SHOW_HINT = 2
const val STAGE_SHOW_ANSWER = 3

val PRIMARY_BUTTON_NORMAL_COLOR: Int = rgb(0x66, 0xcc, 0xff)
val PRIMARY_BUTTON_COMPLETED_COLOR: Int = rgb(0x00, 0xff, 0x80)
val PRIMARY_BUTTON_NOT_COMPLETED_COLOR: Int = rgb(0xFE, 0x9A, 0x2E)

class ExerciseActivity : AppCompatActivity() {

    private lateinit var txtCompleted: TextView

    private lateinit var txtQuestion: TextView
    private lateinit var txtAnswer: TextView
    private lateinit var txtHint: TextView

    private lateinit var btnPrimary: Button
    private lateinit var btnSecondary: Button

    private lateinit var pnlTask: View
    private lateinit var pnlExerciseComplete: View

    private lateinit var userProgress: UserProgress
    private lateinit var qnaSet: QnaSet
    private var stage: Int = STAGE_SHOW_QUESTION
    private var completed: Boolean = false
    private var task: MutableList<QnaItem> = ArrayList()
    private var index: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)


        val upuid: Long = intent.getLongExtra("upuid", 0L)

        if (upuid == 0L) {
            val qsuid: Long  = intent.getLongExtra("qsuid", 0L)
            if (qsuid == 0L) {
                Toast.makeText(this, "Unrecognized QnaSet", Toast.LENGTH_SHORT).show()
                finish()
                return
            }
            val qnaSet = readQnaSet(qsuid)
            if (qnaSet == null) {
                Toast.makeText(this, "Unrecognized Qsuid: ${qsuid.toUidString()}", Toast.LENGTH_SHORT).show()
                finish()
                return
            }
            this.qnaSet = qnaSet
            userProgress = createUserProgress(qnaSet)
        } else {
            val progress: UserProgress? = readUserProgress(upuid)
            if (progress == null) {
                Toast.makeText(this, "Unknown Upuid: ${upuid.toUidString()}", Toast.LENGTH_SHORT).show()
                finish()
                return
            }
            this.userProgress = progress
            val qsuid = progress.qsuid
            val qnaSet = readQnaSet(qsuid)
            if (qnaSet == null) {
                Toast.makeText(this, "Unrecognized Qsuid: ${qsuid.toUidString()}", Toast.LENGTH_SHORT).show()
                finish()
                return
            }
            this.qnaSet = qnaSet

            updateUI()
        }

        decideTask(10, 10)

        txtCompleted = findViewById(R.id.txt_completed)
        txtQuestion = findViewById(R.id.txt_question)
        txtAnswer = findViewById(R.id.txt_answer)
        txtHint = findViewById(R.id.txt_hint)
        btnPrimary = findViewById(R.id.btn_primary)
        btnSecondary = findViewById(R.id.btn_secondary)

        pnlTask = findViewById(R.id.pnl_task)
        pnlExerciseComplete = findViewById(R.id.pnl_exerciseComplete)

        btnPrimary.setOnClickListener { onClickPrimaryButton() }
        btnSecondary.setOnClickListener { onClickSecondaryButton() }

        findViewById<View>(R.id.btn_back).setOnClickListener { finish() }
        findViewById<View>(R.id.btn_completeBack).setOnClickListener { finish() }

        updateUI()
    }

    fun decideTask(newItemCount: Int, oldItemCount: Int) {
        if (userProgress.dailyTaskDate < Date() || userProgress.dailyTaskQiuidList.isEmpty()) {
            val allOldItems = userProgress.completedQiuidSet.mapNotNull { qnaSet.items[it] }
            val allNewItems = qnaSet.items.toList().filter{ it.qiuid !in userProgress.completedQiuidSet }
            task = ArrayList(randomItems(allNewItems, newItemCount) + randomItems(allOldItems, oldItemCount))
        } else {
            task = ArrayList(userProgress.dailyTaskQiuidList.mapNotNull { qnaSet.items[it] })
        }
    }

    fun onClickPrimaryButton() {
        if (stage < STAGE_SHOW_ANSWER) {
            stage = STAGE_SHOW_ANSWER
            completed = true
            updateUI()
        } else {
            nextItem()
        }
    }

    fun onClickSecondaryButton() {
        when {
            stage <= STAGE_SHOW_QUESTION -> {
                stage = STAGE_SHOW_HINT
                completed =false
            }
            stage == STAGE_SHOW_HINT -> {
                stage = STAGE_SHOW_ANSWER
                completed = false
            }
            stage >= STAGE_SHOW_ANSWER -> {
                stage = STAGE_SHOW_ANSWER
                completed = false
            }
        }

        updateUI()
    }

    fun nextItem() {
        if (index >= 0 && index < task.size) {
            userProgress.dailyTaskQiuidList = task.subList(index + 1, task.size).map { it.qiuid }
            val item: QnaItem = task[index]
            if (completed) {
                userProgress.completedQiuidSet.add(item.qiuid)
                index++
            } else {
                task.removeAt(index)
                task.add(item)
            }
        }

        stage = STAGE_SHOW_QUESTION
        completed = false

        updateUI()
    }

    fun updateUI() {
        if (index >= task.size) {
            pnlTask.visibility = View.GONE
            pnlExerciseComplete.visibility = View.VISIBLE
            return
        }
        pnlTask.visibility = View.VISIBLE
        pnlExerciseComplete.visibility = View.GONE

        txtCompleted.text = "${index + if (completed) 1 else 0} / ${task.size} / ${qnaSet.items.size}"

        val item = qnaSet.items.elemAt(index)
        if (item != null) {
            txtQuestion.text = item.question
            txtHint.text = if (stage >= STAGE_SHOW_HINT) item.hint else ""
            txtAnswer.text = if (stage >= STAGE_SHOW_ANSWER) item.answer else ""
        }

        btnPrimary.text = if (completed) "下一个" else "我知道"
        btnSecondary.text = when (stage) {
            STAGE_SHOW_QUESTION -> "提示一下"
            STAGE_SHOW_HINT -> "显示答案"
            STAGE_SHOW_ANSWER -> "标为错误"
            else -> ""
        }

        btnSecondary.isEnabled = !(stage >= STAGE_SHOW_ANSWER && !completed)

        btnPrimary.setBackgroundColor(when {
            completed -> PRIMARY_BUTTON_COMPLETED_COLOR
            stage >= STAGE_SHOW_ANSWER && !completed -> PRIMARY_BUTTON_NOT_COMPLETED_COLOR
            else -> PRIMARY_BUTTON_NORMAL_COLOR
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        writeUserProgress(userProgress)
    }
}