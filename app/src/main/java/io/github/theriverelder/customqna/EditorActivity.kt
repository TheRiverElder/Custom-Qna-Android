package io.github.theriverelder.customqna

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.theriverelder.customqna.adaptors.QnaItemListAdaptor
import io.github.theriverelder.customqna.data.QnaItem
import io.github.theriverelder.customqna.data.QnaSet
import io.github.theriverelder.customqna.utils.toUidString

class EditorActivity : AppCompatActivity() {

    lateinit var drwEditor: DrawerLayout

    lateinit var txtName: TextView

    lateinit var sclContent: ScrollView
    lateinit var rclQnaItemList: RecyclerView
    lateinit var qnaItemListAdaptor: QnaItemListAdaptor

    lateinit var pnlEditInfo: View
    lateinit var pnlEditItem: View
    lateinit var pnlIntro: TextView

    lateinit var edtName: EditText
    lateinit var edtVersion: EditText
    lateinit var edtDescription: EditText
    lateinit var edtQuestion: EditText
    lateinit var edtAnswer: EditText
    lateinit var edtHint: EditText

    lateinit var qnaSet: QnaSet
    var editingItemIndex: Int = -1
    var editingItem: QnaItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)

        val qsuid = intent.getLongExtra("qsuid", 0)

        if (qsuid == 0L) {
            qnaSet = createQnaSet()
        } else {
            val set = readQnaSet(qsuid)
            if (set == null) {
                Toast.makeText(this, "Unknown qsuid: ${qsuid.toUidString()}", Toast.LENGTH_SHORT)
                    .show()
                finish()
                return
            }
            qnaSet = set
        }

        assignViews()
        initDisplay()
        initListeners()
    }

    fun assignViews() {drwEditor = findViewById(R.id.drw_editor)
        txtName = findViewById(R.id.txt_name)

        rclQnaItemList = findViewById(R.id.rcl_qnaItemList)

        sclContent = findViewById(R.id.scl_content)
        pnlEditInfo = LayoutInflater.from(this).inflate(R.layout.fragment_editor_edit_info, sclContent, false)
        pnlEditItem = LayoutInflater.from(this).inflate(R.layout.fragment_editor_edit_item, sclContent, false)
        pnlIntro = TextView(this)
        pnlIntro.text = """
            从左侧划出导航栏
            点击顶部的卡片以编辑题集信息
            点击加号按钮添加条目
            点击条目以编辑
        """.trimIndent()

        edtName = pnlEditInfo.findViewById(R.id.edt_name)
        edtVersion = pnlEditInfo.findViewById(R.id.edt_version)
        edtDescription = pnlEditInfo.findViewById(R.id.edt_description)
        edtQuestion = pnlEditItem.findViewById(R.id.edt_question)
        edtAnswer = pnlEditItem.findViewById(R.id.edt_answer)
        edtHint = pnlEditItem.findViewById(R.id.edt_hint)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rclQnaItemList.layoutManager = layoutManager
        qnaItemListAdaptor = QnaItemListAdaptor(this, qnaSet.items)
        rclQnaItemList.adapter =qnaItemListAdaptor
    }

    fun initDisplay() {
        txtName.text = qnaSet.name
        setContent(Content.INTRO)
    }

    fun initListeners() {
        findViewById<View>(R.id.crd_qnaSetInfo).setOnClickListener { editInfo() }
        findViewById<Button>(R.id.btn_newQnaItem).setOnClickListener { addNewQnaItem() }

        findViewById<View>(R.id.fab_openDrawer).setOnClickListener { drwEditor.openDrawer(GravityCompat.START) }
        findViewById<View>(R.id.fab_togglePreview).setOnClickListener { showPreview() }

        pnlEditInfo.setOnClickListener { editInfo() }

        edtName.addTextChangedListener(MyTextWatcher(false) { txtName.text = it; qnaSet.name = it })
        edtVersion.addTextChangedListener(MyTextWatcher(false) { qnaSet.version = it })
        edtDescription.addTextChangedListener(MyTextWatcher(false) { qnaSet.description = it })
        edtQuestion.addTextChangedListener(MyTextWatcher(true) { editingItem?.question = it })
        edtAnswer.addTextChangedListener(MyTextWatcher(true) { editingItem?.answer = it })
        edtHint.addTextChangedListener(MyTextWatcher(false) { editingItem?.hint = it })

        pnlEditItem.findViewById<Button>(R.id.btn_deleteQnaItem).setOnClickListener { deleteItemAt(editingItemIndex) }
        pnlEditItem.findViewById<Button>(R.id.btn_insertNext).setOnClickListener { addNewQnaItem() }
    }

    override fun onDestroy() {
        super.onDestroy()
        save()
    }

    fun save() {
        val success = writeQnaSet(qnaSet)
        Toast.makeText(this, "Save ${if (success) "succeeded" else "failed"}", Toast.LENGTH_LONG).show()
    }

    fun editInfo() {
        setContent(Content.EDIT_INFO)

        edtName.setText(qnaSet.name)
        edtVersion.setText(qnaSet.version)
        edtDescription.setText(qnaSet.description)

        drwEditor.closeDrawers()
    }

    fun editItemAt(itemIndex: Int) {
        if (itemIndex < 0) {
            editingItemIndex = -1
            editingItem = null
        }

        this.editingItemIndex = itemIndex
        this.editingItem = qnaSet.items.elemAt(itemIndex)

        setContent(Content.EDIT_ITEM)

        edtQuestion.setText(editingItem?.question ?: "")
        edtAnswer.setText(editingItem?.answer ?: "")
        edtHint.setText(editingItem?.hint ?: "")

        drwEditor.closeDrawers()
    }

    fun addNewQnaItem() {
        val index = qnaSet.items.size
        val item = qnaSet.createNextItem()
        qnaSet.items.put(item)
        qnaItemListAdaptor.notifyItemInserted(index)
        editItemAt(index)
    }

    fun deleteItemAt(index: Int) {
        if (index < 0) return

        val item = qnaSet.items.remove(index)
        if (item != null) {
            editingItem?.let {
                if (item.qiuid == it.qiuid) {
                    this.editingItem = null
                    pnlEditItem.visibility = View.GONE
                    setContent(Content.INTRO)
                }
            }
        }
        qnaItemListAdaptor.notifyItemRemoved(index)
    }

    fun showPreview() {
        drwEditor.openDrawer(GravityCompat.END)
    }

    fun setContent(content: Content) {
        sclContent.removeAllViews()
        val view: View? = when (content) {
            Content.EDIT_INFO -> pnlEditInfo
            Content.EDIT_ITEM -> pnlEditItem
            Content.INTRO -> pnlIntro
            Content.PREVIEW -> null
        }
        if (view != null) {
            sclContent.addView(view)
        }
    }

    inner class MyTextWatcher(
        private val doNotifyDataChange: Boolean,
        val cb: (String) -> Unit
    ) : TextWatcher {

        override fun afterTextChanged(s: Editable?) { }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            cb(s?.toString() ?: "")
            if (doNotifyDataChange && editingItemIndex >= 0) {
                qnaItemListAdaptor.notifyItemChanged(editingItemIndex)
            }
        }

    }

    enum class Content {
        EDIT_INFO,
        EDIT_ITEM,
        PREVIEW,
        INTRO
    }
}
