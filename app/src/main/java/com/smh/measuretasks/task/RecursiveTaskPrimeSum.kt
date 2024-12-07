package com.smh.measuretasks.task

import android.util.Log
import com.smh.measuretasks.helper.COMPUTE_PRIME_SUM_THRESHOLD
import com.smh.measuretasks.helper.MEASURE_DELAY
import com.smh.measuretasks.helper.MEASURE_STEP_TAG
import com.smh.measuretasks.helper.PRIME_SUM_RANGE_END
import com.smh.measuretasks.helper.isPrime
import com.smh.measuretasks.helper.postMainThread
import java.util.concurrent.ExecutorService
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.RecursiveTask
import kotlin.system.measureTimeMillis

fun forkJoinMeasure(
    setCalculating: (Boolean) -> Unit,
    setResult: (String) -> Unit,
    setTimeDifference: (Long) -> Unit,
    executor: ExecutorService,
    forkJoinPool: ForkJoinPool,
    onComplete: () -> Unit
) {
    executor.execute {
        postMainThread {
            setCalculating(true)
            setResult("")
        }

        var result = 0L
        val timeDifference = measureTimeMillis {
            result = forkJoinPool.invoke(RecursiveTaskPrimeSum(1, PRIME_SUM_RANGE_END))
        }

        postMainThread {
            setResult(result.toString())
            setTimeDifference(timeDifference)
            setCalculating(false)
        }

        Thread.sleep(MEASURE_DELAY)

        postMainThread {
            Log.i(MEASURE_STEP_TAG, "ForkJoinMeasure: Complete")
            onComplete()
        }
    }
}

private class RecursiveTaskPrimeSum(
    private val start: Int,
    private val end: Int,
): RecursiveTask<Long>() {
    override fun compute(): Long {
        return if ((end - start) <= COMPUTE_PRIME_SUM_THRESHOLD) {
            var sum = 0L
            for (i in start..end) {
                if (isPrime(i)) {
                    sum += i
                }
            }
            sum
        } else {
            val mid = (start + end) / 2
            val leftSumTask = RecursiveTaskPrimeSum(start, mid)
            val rightSumTask = RecursiveTaskPrimeSum(mid + 1, end)

            leftSumTask.fork()
            val rightSum = rightSumTask.compute()
            leftSumTask.join() + rightSum
        }
    }
}