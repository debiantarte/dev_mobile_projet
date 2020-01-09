package com.example.dm_project.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.dm_project.Task
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class TasksRepository {
    private val tasksService = API.INSTANCE.tasksService
    private val coroutineScope = MainScope()


    fun getTasks(): LiveData<List<Task>?> {
        val tasks = MutableLiveData<List<Task>?>()
        coroutineScope.launch {
            tasks.postValue( loadTasks()) }
        return tasks
    }

    private suspend fun loadTasks(): List<Task>? {
        val taskReponse = tasksService.getTasks()
        return if(taskReponse.isSuccessful) taskReponse.body() else null
    }
}