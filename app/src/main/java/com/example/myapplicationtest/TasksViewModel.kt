package com.example.dm_project


import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.dm_project.network.TasksRepository
//import com.example.td2.network.TaskRepository

class TasksViewModel : ViewModel(){
    private val repository = TasksRepository()
    private var tasks = mutableListOf<Task>()
    val tasksAdapter = TasksAdapter(tasks)

    fun loadTasks(lifecycle: LifecycleOwner) {
        repository.getTasks().observe(
            lifecycle,
            Observer {
                if(it==null)return@Observer
                tasks.clear()
                tasks.addAll(it)
                tasksAdapter.notifyDataSetChanged()

            })
    }
}