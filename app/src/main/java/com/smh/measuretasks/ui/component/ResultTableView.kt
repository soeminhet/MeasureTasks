package com.smh.measuretasks.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import com.smh.measuretasks.helper.ThreadType
import com.smh.measuretasks.helper.tableHeader
import com.smh.measuretasks.ui.theme.MeasureTasksTheme

@Composable
fun ResultTableView(
    modifier: Modifier = Modifier,
    calculatedResult: Map<ThreadType, List<Long>>
) {
    Column(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            tableHeader.forEachIndexed() { index, value ->
                Text(
                    text = value,
                    modifier = Modifier.weight(1f),
                    textAlign = if (index == 0) TextAlign.Start else TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        ThreadType.entries.fastForEach { threadType ->
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = threadType.name,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start,
                )
                calculatedResult[threadType]?.fastForEach {
                    Text(
                        text = it.toString(),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ResultTablePreView() {
    MeasureTasksTheme {
        ResultTableView(
            calculatedResult = mapOf(
                ThreadType.IO to listOf(100L, 200L, 300L),
                ThreadType.DEFAULT to listOf(400L, 500L, 600L),
                ThreadType.FORK_JOIN to listOf(700L, 800L, 900L),
                ThreadType.EXECUTOR to listOf(1000L, 1100L, 1200L)
            )
        )
    }
}