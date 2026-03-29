package com.example.taskmanagerapp

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import android.util.Log

class ReminderWorker(context: Context, params: WorkerParameters) :
    Worker(context, params) {

    override fun doWork(): Result {
        Log.d("WORKER", "Reminder triggered!")
        return Result.success()
    }
}
