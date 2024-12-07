package com.smh.measuretasks.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smh.measuretasks.helper.CPUUsageMonitor
import com.smh.measuretasks.helper.highPriorityDispatcher
import com.smh.measuretasks.ui.theme.MeasureTasksTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext

@Composable
fun CPUUsageMonitorView(
    modifier: Modifier = Modifier
) {
    val cpuUsageMonitor = remember { CPUUsageMonitor() }
    val cpuUsage by produceState(initialValue = 0f) {
        withContext(highPriorityDispatcher) {
            while (this.isActive) {
                value = cpuUsageMonitor.monitorCPULoad() * 0.01f
                delay(600)
            }
        }
    }

    Column(modifier = modifier) {
        Text(
            text = "CPU Usage",
            modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)
        )

        LinearProgressIndicator(
            progress = { cpuUsage },
            strokeCap = StrokeCap.Round,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
                .height(10.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CPUUsageMonitorPreView() {
    MeasureTasksTheme {
        CPUUsageMonitorView()
    }
}