package com.example.m7019e

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class NetworkWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val isConnected = inputData.getBoolean("isConnected", false)
        Log.d("NetworkWorker", "Network connectivity changed: $isConnected")
        return Result.success()
    }
}
