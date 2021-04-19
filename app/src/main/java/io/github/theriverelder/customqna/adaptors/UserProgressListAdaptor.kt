package io.github.theriverelder.customqna.adaptors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import io.github.theriverelder.customqna.MainActivity
import io.github.theriverelder.customqna.Manifest
import io.github.theriverelder.customqna.R
import io.github.theriverelder.customqna.data.UserProgressInfo
import io.github.theriverelder.customqna.utils.toUidString
import java.util.*

class UserProgressListAdaptor(val activity: MainActivity, var data: List<UserProgressInfo>)
    : RecyclerView.Adapter<UserProgressListAdaptor.UserProgressViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserProgressViewHolder {
        val view = LayoutInflater.from(activity).inflate(R.layout.item_user_progress_info, parent, false)
        val viewHolder = UserProgressViewHolder(view)
        view.setOnClickListener { Toast.makeText(activity, "Current date is ${Date()}", Toast.LENGTH_SHORT).show() }
        return viewHolder
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: UserProgressViewHolder, position: Int) {
        val info = data[position]
        val qnaSetInfo = Manifest.qnaSetInfoMap[info.qsuid]
        holder.txtName.text = qnaSetInfo?.name ?: "???"
        holder.txtProgress.text = "${info.completedItemCount} / ${qnaSetInfo?.itemsCount ?: "?"}"
        holder.txtUpuid.text = info.upuid.toUidString()
        holder.txtQsuid.text = (qnaSetInfo?.qsuid ?: 0L).toUidString()
        holder.itemView.setOnClickListener { activity.startProgress(info) }
    }

    inner class UserProgressViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtName: TextView = view.findViewById(R.id.txt_name)
        val txtProgress: TextView = view.findViewById(R.id.txt_progress)
        val txtUpuid: TextView = view.findViewById(R.id.txt_upuid)
        val txtQsuid: TextView = view.findViewById(R.id.txt_qsuid)
    }
}