package io.github.theriverelder.customqna.adaptors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.github.theriverelder.customqna.EditorActivity
import io.github.theriverelder.customqna.R
import io.github.theriverelder.customqna.data.QnaItem
import io.github.theriverelder.customqna.utils.OrderedMap

class QnaItemListAdaptor(val activity: EditorActivity, val data: OrderedMap<QnaItem, Int>)
    : RecyclerView.Adapter<QnaItemListAdaptor.QnaItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QnaItemViewHolder {
        val view = LayoutInflater.from(activity).inflate(R.layout.item_qna_item, parent, false)
        return QnaItemViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: QnaItemViewHolder, position: Int) {
        val elem = data.elemAt(position)
        if (elem != null) {
            holder.txtQuestion.text = elem.question
            holder.txtAnswer.text = elem.answer
            holder.itemView.setOnClickListener { activity.editItemAt(position) }
        }
    }

    inner class QnaItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtQuestion: TextView = view.findViewById(R.id.txt_question)
        val txtAnswer: TextView = view.findViewById(R.id.txt_answer)
    }
}