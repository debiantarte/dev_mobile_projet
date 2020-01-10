package com.example.dm_project

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.dm_project.network.API
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class TasksAdapter(private val tasks: MutableList<Task>, val context: Context) : RecyclerView.Adapter<TaskViewHolder>() {
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
        val taskEditIntent = Intent(context, TaskEditActivity(tasks[position].id, tasks[position].description, tasks[position].title)::class.java)
        val editImage = holder.itemView.findViewById<ImageView>(R.id.task_edit)
        editImage.setOnClickListener{
            context.startActivity(taskEditIntent)
        }
    }

    fun onDeleteClickListener(task: Task)
    {
        val position = tasks.indexOf(task)
        //val title = tasks[position].title
        tasks.remove(task)
        MainScope().launch {
            API.INSTANCE.tasksService.deleteTask(task.id)
        }
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, tasks.size)
    }
}