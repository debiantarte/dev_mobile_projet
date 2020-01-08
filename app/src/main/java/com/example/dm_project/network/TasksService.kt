package com.example.dm_project.network

import com.example.dm_project.Task
import retrofit2.Response
import retrofit2.http.*

interface TasksService {
    @GET("tasks")
    suspend fun getTasks(): Response<List<Task>>

    @DELETE("tasks/{id}")
    suspend fun deleteTask(@Path("id") id: String): Response<String>

    @POST("tasks")
    suspend fun createTask(@Body task: Task): Response<Task>

    @PATCH("tasks/{id}")
    suspend fun updateTask(@Path("id") id: String, @Body task: Task): Response<Task>
}