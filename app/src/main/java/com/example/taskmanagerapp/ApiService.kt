package com.example.taskmanagerapp

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("todos")
    fun getTasks(): Call<List<Todo>>
}
