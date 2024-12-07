package com.smh.measuretasks.ui.component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smh.measuretasks.ui.theme.MeasureTasksTheme

@Composable
fun RowScope.RunStatus(
    modifier: Modifier = Modifier,
    calculatedResult: String,
    label: String,
    calculating: Boolean,
) {
    Column(
        modifier = modifier
            .weight(1f)
            .aspectRatio(1f)
            .clip(shape = MaterialTheme.shapes.medium)
            .background(Color.LightGray, shape = MaterialTheme.shapes.medium),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Crossfade(
            targetState = calculating,
            label = "CalculateContainer",
            modifier = Modifier
                .height(30.dp)
                .fillMaxWidth()
        ) {
            if (it) {
                Box(
                    modifier = Modifier
                        .height(30.dp)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    LinearProgressIndicator(
                        modifier = Modifier.width(50.dp)
                    )
                }
            } else {
                Text(
                    text = calculatedResult,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }


        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}

@Preview
@Composable
private fun RunStatusPreview() {
    MeasureTasksTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            RunStatus(
                label = "Label",
                calculatedResult = "Calculated Result",
                calculating = false
            )

            RunStatus(
                label = "Label",
                calculatedResult = "Calculated Result",
                calculating = true
            )
        }
    }
}