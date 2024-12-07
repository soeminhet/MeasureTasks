package com.smh.measuretasks.helper

import android.os.Handler
import android.os.Looper
import android.os.Process
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

val coresCount: Int get() = Runtime.getRuntime().availableProcessors()

val highPriorityDispatcher: CoroutineDispatcher = Executors.newFixedThreadPool(1) { runnable ->
    Thread(runnable).apply {
        priority = Thread.MAX_PRIORITY
        Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_DISPLAY)
    }
}.asCoroutineDispatcher()

@OptIn(ExperimentalCoroutinesApi::class)
val ioDispatcherWithCoreCountParallelism: CoroutineDispatcher = Dispatchers.IO.limitedParallelism(
    parallelism = coresCount
)

fun postMainThread(runnable: () -> Unit) {
    Handler(Looper.getMainLooper()).post(runnable)
}