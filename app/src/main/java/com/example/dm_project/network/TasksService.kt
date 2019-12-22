package com.example.dm_project.network

import com.example.dm_project.Task
import retrofit2.Response
import retrofit2.http.GET

interface TasksService {
    @GET("tasks")
    suspend fun getTasks(): Response<List<Task>>
}