package com.example.dm_project

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.dm_project.network.API
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class TasksAdapter(private val tasks: MutableList<Task>) : RecyclerView.Adapter<TaskViewHolder>() {
    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false))
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
        val deleteImage = holder.itemView.findViewById<ImageView>(R.id.task_delete)
        deleteImage.setOnClickListener{
            onDeleteClickListener(tasks[position])
        }
    }

    fun onDeleteClickListener(task: Task)
    {
        val position = tasks.indexOf(task)
        tasks.remove(task)
        val title = tasks[position].title
        MainScope().launch {
            API.INSTANCE.tasksService.deleteTask("id_$title")
        }
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, tasks.size)
    }
}