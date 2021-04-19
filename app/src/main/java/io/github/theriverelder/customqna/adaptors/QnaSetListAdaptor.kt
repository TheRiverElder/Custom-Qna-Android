package io.github.theriverelder.customqna.adaptors

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import io.github.theriverelder.customqna.R
import io.github.theriverelder.customqna.data.QnaSetInfo
import io.github.theriverelder.customqna.utils.toUidString
import java.util.*

class QnaSetListAdaptor(private val context: Context, var data: List<QnaSetInfo>)
    : RecyclerView.Adapter<QnaSetListAdaptor.QnaSetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QnaSetViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_qna_set_info, parent, false)
        val viewHolder = QnaSetViewHolder(view)
        view.setOnClickListener { Toast.makeText(context, "Current date is ${Date()}", Toast.LENGTH_SHORT).show() }
        return viewHolder
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: QnaSetViewHolder, position: Int) {
        val item = data[position]
        holder.txtName.text = item.name
        holder.txtVersion.text = item.version
        holder.txtQsuid.text = item.qsuid.toUidString()
        holder.txtDescription.text = item.description
    }

    inner class QnaSetViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val txtName: TextView = view.findViewById(R.id.txt_name)
        val txtVersion: TextView = view.findViewById(R.id.txt_version)
        val txtQsuid: TextView = view.findViewById(R.id.txt_qsuid)
        val txtDescription: TextView = view.findViewById(R.id.txt_description)
    }


}