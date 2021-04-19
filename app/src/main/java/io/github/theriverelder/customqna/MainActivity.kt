package io.github.theriverelder.customqna

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.theriverelder.customqna.adaptors.UserProgressListAdaptor
import io.github.theriverelder.customqna.data.QnaSetInfo
import io.github.theriverelder.customqna.data.UserProgressInfo

class MainActivity : AppCompatActivity() {

    lateinit var rclUserProgressList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setup(this)

        this.findViewById<Button>(R.id.btn_manageQnaSetList).setOnClickListener {
            val intent = Intent(this, QnaSetManagerActivity::class.java)
            startActivity(intent)
        }

        val qnaSetInfo = QnaSetInfo(4, "1.0.1", "wevtvs", "dd sgssd", 100)

        rclUserProgressList = findViewById(R.id.rcl_userProgressList)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rclUserProgressList.layoutManager = layoutManager
        rclUserProgressList.adapter = UserProgressListAdaptor(this, listOf())
    }

    fun startProgress(userProgressInfo: UserProgressInfo) {
        val intent = Intent(this, ExerciseActivity::class.java)
        intent.putExtra("upuid", userProgressInfo.upuid)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onDestroy() {
        super.onDestroy()
        dispose()
    }
}
