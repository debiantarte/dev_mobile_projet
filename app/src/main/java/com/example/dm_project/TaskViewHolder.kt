package com.example.dm_project

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_task.view.*

class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(task: Task) {
        itemView.task_title.text = task.title
        itemView.task_description.text = task.description
        itemView.task_edit.setOnClickListener {
            onEditClickListener(task)
        }
    }

    private fun onEditClickListener(task: Task) {
        val intent = Intent(itemView.context, TaskFormActivity::class.java)
        intent.putExtra("id", task.id)
        intent.putExtra("title", task.title)
        intent.putExtra("description", task.description)
        itemView.context.startActivity(intent)
    }
}