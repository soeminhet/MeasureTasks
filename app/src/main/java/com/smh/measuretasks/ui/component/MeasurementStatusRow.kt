package com.smh.measuretasks.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smh.measuretasks.ui.theme.MeasureTasksTheme

@Composable
fun MeasurementStatusRow(
    label1: String,
    calculatedResult1: () -> String,
    calculating1: () -> Boolean,
    label2: String,
    calculatedResult2: () -> String,
    calculating2: () -> Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        RunStatus(
            label = label1,
            calculatedResult = calculatedResult1(),
            calculating = calculating1()
        )
        RunStatus(
            label = label2,
            calculatedResult = calculatedResult2(),
            calculating = calculating2()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MeasurementStatusRowPreview() {
    MeasureTasksTheme {
        MeasurementStatusRow(
            label1 = "Label 1",
            calculatedResult1 = { "Calculated Result 1" },
            calculating1 = { false },
            label2 = "Label 2",
            calculatedResult2 = { "Calculated Result 2" },
            calculating2 = { true }
        )
    }
}