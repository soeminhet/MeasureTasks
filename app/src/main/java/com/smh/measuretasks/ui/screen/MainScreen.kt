package com.smh.measuretasks.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smh.measuretasks.helper.MEASURE_STEP_TAG
import com.smh.measuretasks.helper.MEASURE_TIME
import com.smh.measuretasks.helper.ThreadType
import com.smh.measuretasks.helper.ioDispatcherWithCoreCountParallelism
import com.smh.measuretasks.task.executorMeasure
import com.smh.measuretasks.task.forkJoinMeasure
import com.smh.measuretasks.task.suspendMeasure
import com.smh.measuretasks.ui.component.CPUUsageMonitorView
import com.smh.measuretasks.ui.component.CalculateResultView
import com.smh.measuretasks.ui.component.MeasurementStatusRow
import com.smh.measuretasks.ui.component.ResultTableView
import com.smh.measuretasks.ui.theme.MeasureTasksTheme
import kotlinx.coroutines.Dispatchers
import java.util.concurrent.Executors
import java.util.concurrent.ForkJoinPool

@Composable
fun MainScreen() {
    val coroutineScope = rememberCoroutineScope()
    val forkAndJoinPool = remember { ForkJoinPool() }
    val executor = remember { Executors.newSingleThreadExecutor() }

    var ioTimeDifference by remember { mutableLongStateOf(0L) }
    var defaultTimeDifference by remember { mutableLongStateOf(0L) }
    var forkJoinTimeDifference by remember { mutableLongStateOf(0L) }
    var executorTimeDifference by remember { mutableLongStateOf(0L) }

    var ioCalculating by remember { mutableStateOf(false) }
    var defaultCalculating by remember { mutableStateOf(false) }
    var forkJoinCalculating by remember { mutableStateOf(false) }
    var executorCalculating by remember { mutableStateOf(false) }

    var calculateResult by remember { mutableStateOf("") }
    var measuring by remember { mutableStateOf(false) }

    val iterationSize = remember { (ThreadType.entries.size * MEASURE_TIME) + 1 }
    var remainingIterations by remember { mutableIntStateOf(iterationSize) }
    val measureResult = remember { mutableStateMapOf<ThreadType, List<Long>>() }
    val calculatedResult = remember { mutableStateMapOf<ThreadType, List<Long>>() }

    fun calculateResult() {
        calculatedResult.clear()
        calculatedResult.putAll(
            measureResult.mapValues {
                val min = it.value.min()
                val mid = it.value.sum() / MEASURE_TIME
                val max = it.value.max()
                listOf(min, mid, max)
            }
        )
        measureResult.clear()
    }

    fun updateMeasureResult(threadType: ThreadType, value: Long) {
        val mapValue = measureResult[threadType]?.toMutableList() ?: mutableListOf()
        mapValue.add(value)
        measureResult[threadType] = mapValue
    }

    fun startMeasuring() {
        if (remainingIterations == iterationSize) {
            // Initialize and warm up
            remainingIterations --

            measureResult.clear()
            calculatedResult.clear()

            suspendMeasure(
                setCalculating = { defaultCalculating = it },
                setResult = {},
                setTimeDifference = {},
                dispatcher = Dispatchers.Default,
                coroutineScope = coroutineScope,
                onComplete = {
                    Log.i(MEASURE_STEP_TAG, "DefaultMeasure: First Complete")
                    startMeasuring()
                }
            )
        } else if (remainingIterations > 0) {
            remainingIterations--

            val threadType = ThreadType.entries.filter {
                measureResult.getOrDefault(it, emptyList()).size < MEASURE_TIME
            }.random()

            Log.i(MEASURE_STEP_TAG, "Start $threadType")

            when(threadType) {
                ThreadType.IO -> {
                    suspendMeasure(
                        setCalculating = { ioCalculating = it },
                        setResult = { calculateResult = it },
                        setTimeDifference = {
                            ioTimeDifference = it
                            updateMeasureResult(
                                threadType = ThreadType.IO,
                                value = it
                            )
                        },
                        dispatcher = ioDispatcherWithCoreCountParallelism,
                        coroutineScope = coroutineScope,
                        onComplete = {
                            Log.i(MEASURE_STEP_TAG, "IOMeasure: Complete")
                            startMeasuring()
                        }
                    )
                }
                ThreadType.DEFAULT -> {
                    suspendMeasure(
                        setCalculating = { defaultCalculating = it },
                        setResult = { calculateResult = it },
                        setTimeDifference = {
                            defaultTimeDifference = it
                            updateMeasureResult(
                                threadType = ThreadType.DEFAULT,
                                value = it
                            )
                        },
                        dispatcher = Dispatchers.Default,
                        coroutineScope = coroutineScope,
                        onComplete = {
                            Log.i(MEASURE_STEP_TAG, "DefaultMeasure: Complete")
                            startMeasuring()
                        }
                    )
                }
                ThreadType.FORK_JOIN -> {
                    forkJoinMeasure(
                        setCalculating = { forkJoinCalculating = it },
                        setResult = { calculateResult = it },
                        setTimeDifference = {
                            forkJoinTimeDifference = it
                            updateMeasureResult(
                                threadType = ThreadType.FORK_JOIN,
                                value = it
                            )
                        },
                        executor = executor,
                        forkJoinPool = forkAndJoinPool,
                        onComplete = { startMeasuring() }
                    )
                }
                ThreadType.EXECUTOR -> {
                    executorMeasure(
                        setCalculating = { executorCalculating = it },
                        setResult = { calculateResult = it },
                        setTimeDifference = {
                            executorTimeDifference = it
                            updateMeasureResult(
                                threadType = ThreadType.EXECUTOR,
                                value = it
                            )
                        },
                        executor = executor,
                        onComplete = { startMeasuring() }
                    )
                }
            }
        } else {
            measuring = false
            remainingIterations = iterationSize
            calculateResult()
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            CPUUsageMonitorView()

            Spacer(modifier = Modifier.weight(1f))

            if (calculatedResult.isNotEmpty()) {
                ResultTableView(calculatedResult = calculatedResult)
            } else {
                CalculateResultView(
                    calculateResult = { calculateResult }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            MeasurementStatusRow(
                label1 = "IO Dispatcher",
                calculatedResult1 = { "$ioTimeDifference ms" },
                calculating1 = { ioCalculating },
                label2 = "Default Dispatcher",
                calculatedResult2 = { "$defaultTimeDifference ms" },
                calculating2 = { defaultCalculating }
            )

            MeasurementStatusRow(
                label1 = "Fork&Join",
                calculatedResult1 = { "$forkJoinTimeDifference ms" },
                calculating1 = { forkJoinCalculating },
                label2 = "Executor",
                calculatedResult2 = { "$executorTimeDifference ms" },
                calculating2 = { executorCalculating }
            )

            Button(
                onClick = {
                    measuring = true
                    startMeasuring()
                },
                enabled = !measuring,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(
                    text = "Start Measure",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    MeasureTasksTheme {
        MainScreen()
    }
}