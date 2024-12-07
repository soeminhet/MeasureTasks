package com.smh.measuretasks.task

import com.smh.measuretasks.helper.COMPUTE_PRIME_SUM_THRESHOLD
import com.smh.measuretasks.helper.MEASURE_DELAY
import com.smh.measuretasks.helper.PRIME_SUM_RANGE_END
import com.smh.measuretasks.helper.isPrime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

fun suspendMeasure(
    setCalculating: (Boolean) -> Unit,
    setResult: (String) -> Unit,
    setTimeDifference: (Long) -> Unit,
    onComplete: () -> Unit,
    coroutineScope: CoroutineScope,
    dispatcher: CoroutineDispatcher
) {
    coroutineScope.launch(dispatcher) {
        setCalculating(true)
        setResult("")

        val timeDifference = withContext(dispatcher) {
            measureTimeMillis {
                val result = computePrimeSum(
                    start = 1,
                    end = PRIME_SUM_RANGE_END,
                    dispatcher = dispatcher
                )
                setResult(result.toString())
            }
        }

        setTimeDifference(timeDifference)
        setCalculating(false)

        delay(MEASURE_DELAY)
    }.invokeOnCompletion {
        onComplete()
    }
}

private suspend fun computePrimeSum(
    start: Int,
    end: Int,
    dispatcher: CoroutineDispatcher
): Long {
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
        val leftSumDeferred = CoroutineScope(dispatcher).async { computePrimeSum(start, mid, dispatcher) }
        val rightSum = computePrimeSum(mid + 1, end, dispatcher)
        leftSumDeferred.await() + rightSum
    }
}
