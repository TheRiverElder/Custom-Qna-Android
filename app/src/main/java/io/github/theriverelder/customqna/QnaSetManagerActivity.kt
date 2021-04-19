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
        rclQnaSetList.adapter = QnaSetListAdaptor(this, listOf(
            QnaSetInfo(1, "1.0.1", "aaa", "ddd", 100),
            QnaSetInfo(2, "1.0.1", "sbvs", "wrvw", 100),
            QnaSetInfo(3, "1.0.1", "hese", "vrv", 100),
            QnaSetInfo(4, "1.0.1", "wevtvs", "dd sgssd", 100)
        ))
    }

    fun addNewQnaSetAndEdit() {
        val intent = Intent(this, EditorActivity::class.java)
        intent.putExtra("qsuid", 0L)
        startActivity(intent)
    }
}