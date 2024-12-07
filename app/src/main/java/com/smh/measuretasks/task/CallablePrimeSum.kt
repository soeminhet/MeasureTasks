package com.smh.measuretasks.task

import android.util.Log
import com.smh.measuretasks.helper.COMPUTE_PRIME_SUM_THRESHOLD
import com.smh.measuretasks.helper.MEASURE_DELAY
import com.smh.measuretasks.helper.MEASURE_STEP_TAG
import com.smh.measuretasks.helper.PRIME_SUM_RANGE_END
import com.smh.measuretasks.helper.isPrime
import com.smh.measuretasks.helper.postMainThread
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.system.measureTimeMillis

fun executorMeasure(
    setCalculating: (Boolean) -> Unit,
    setResult: (String) -> Unit,
    setTimeDifference: (Long) -> Unit,
    executor: ExecutorService,
    onComplete: () -> Unit
) {
    executor.execute {
        postMainThread {
            setCalculating(true)
            setResult("")
        }

        var result = 0L
        val timeDifference = measureTimeMillis {
            result = CallablePrimeSum(1, PRIME_SUM_RANGE_END).call()
        }

        postMainThread {
            setResult(result.toString())
            setTimeDifference(timeDifference)
            setCalculating(false)
        }

        Thread.sleep(MEASURE_DELAY)

        postMainThread {
            Log.i(MEASURE_STEP_TAG, "ExecutorMeasure: Complete")
            onComplete()
        }
    }
}

private class CallablePrimeSum(
    private val start: Int,
    private val end: Int
): Callable<Long> {
    override fun call(): Long {
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
            val leftSumTask = CallablePrimeSum(start, mid)
            val rightSumTask = CallablePrimeSum(mid + 1, end)

            val executor = Executors.newSingleThreadExecutor()
            val leftSum = executor.submit(leftSumTask)
            val rightSum = rightSumTask.call()
            leftSum.get() + rightSum
        }
    }
}