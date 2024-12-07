package com.smh.measuretasks

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import com.smh.measuretasks.helper.MEASURE_DELAY
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
import com.smh.measuretasks.ui.component.RunStatus
import com.smh.measuretasks.ui.screen.MainScreen
import com.smh.measuretasks.ui.theme.MeasureTasksTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import java.util.concurrent.ForkJoinPool

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MeasureTasksTheme {
                MainScreen()
            }
        }
    }
}

