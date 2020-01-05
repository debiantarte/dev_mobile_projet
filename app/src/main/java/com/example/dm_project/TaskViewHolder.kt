package com.example.dm_project

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_task.view.*

class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(task: Task) {
        itemView.task_title.text = task.title
        itemView.task_description.text = task.description
        //itemView.task_id.text = task.id
    }
}