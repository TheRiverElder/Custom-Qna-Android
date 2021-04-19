package io.github.theriverelder.customqna

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.theriverelder.customqna.adaptors.QnaSetListAdaptor
import io.github.theriverelder.customqna.data.QnaSetInfo

class QnaSetManagerActivity : AppCompatActivity() {

    lateinit var rclQnaSetList: RecyclerView
    lateinit var qnaSetListAdaptor: QnaSetListAdaptor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qna_set_manager)

        findViewById<Button>(R.id.btn_back).setOnClickListener { finish() }
//        findViewById<Button>(R.id.btn_load).setOnClickListener {  }
        findViewById<Button>(R.id.btn_new).setOnClickListener { addNewQnaSetAndEdit() }

        rclQnaSetList = findViewById(R.id.rcl_qnaSetList)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rclQnaSetList.layoutManager = layoutManager
        qnaSetListAdaptor = QnaSetListAdaptor(this, Manifest.qnaSetInfoMap.values.toList())
        rclQnaSetList.adapter = qnaSetListAdaptor
    }

    override fun onResume() {
        super.onResume()
        qnaSetListAdaptor.data = Manifest.qnaSetInfoMap.values.toList()
        qnaSetListAdaptor.notifyDataSetChanged()
    }

    fun addNewQnaSetAndEdit() {
        val intent = Intent(this, EditorActivity::class.java)
        intent.putExtra("qsuid", 0L)
        startActivity(intent)
    }

    fun editQnaSet(qnaSetInfo: QnaSetInfo) {
        val intent = Intent(this, EditorActivity::class.java)
        intent.putExtra("qsuid", qnaSetInfo.qsuid)
        startActivity(intent)
    }

    fun removeQnaSet(qnaSetInfo: QnaSetInfo) {
        Manifest.qnaSetInfoMap.remove(qnaSetInfo.qsuid)
        deleteQnaSet(qnaSetInfo.qsuid)
        qnaSetListAdaptor.data = Manifest.qnaSetInfoMap.values.toList()
        qnaSetListAdaptor.notifyDataSetChanged()
    }

    fun startNewProgress(qnaSetInfo: QnaSetInfo) {
        val intent = Intent(this, ExerciseActivity::class.java)
        intent.putExtra("upuid", 0)
        intent.putExtra("qsuid", qnaSetInfo.qsuid)
        startActivity(intent)
    }
}