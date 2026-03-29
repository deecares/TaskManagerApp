package com.example.taskmanagerapp

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DashboardActivity : AppCompatActivity() {

    private val taskList = mutableListOf<Task>()
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val btnAdd = findViewById<Button>(R.id.btnAdd)

        // initial data
        taskList.add(Task("Study Android"))
        taskList.add(Task("Do Assignment"))

        adapter = TaskAdapter(taskList)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)

        api.getTasks().enqueue(object : Callback<List<Todo>> {
            override fun onResponse(
                call: Call<List<Todo>>,
                response: Response<List<Todo>>
            ) {
                if (response.isSuccessful) {
                    val todos = response.body()
                    if (todos != null) {
                        taskList.clear()
                        for (todo in todos.take(5)) {
                            taskList.add(Task(todo.title))
                        }
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<List<Todo>>, t: Throwable) {
                t.printStackTrace()
            }
        })

        // 👉 WORKMANAGER REMINDER
        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>().build()
        WorkManager.getInstance(this).enqueue(workRequest)

        // 👉 ADD TASK BUTTON CLICK
        btnAdd.setOnClickListener {
            showAddDialog()
        }
    }

    private fun showAddDialog() {
        val editText = EditText(this)

        AlertDialog.Builder(this)
            .setTitle("Add Task")
            .setView(editText)
            .setPositiveButton("Add") { _, _ ->
                val taskTitle = editText.text.toString()
                if (taskTitle.isNotEmpty()) {
                    taskList.add(Task(taskTitle))
                    adapter.notifyDataSetChanged()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
