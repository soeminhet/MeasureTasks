package com.smh.measuretasks.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.smh.measuretasks.helper.PRIME_SUM_RANGE_END
import com.smh.measuretasks.ui.theme.MeasureTasksTheme

@Composable
fun CalculateResultView(
    modifier: Modifier = Modifier,
    calculateResult: () -> String,
) {
    Column(modifier = modifier) {
        Text(
            text = "Calculating the sum of prime numbers in the range 1 to $PRIME_SUM_RANGE_END.",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = calculateResult().ifEmpty { "..." },
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.displaySmall
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CalculateResultPreView() {
    MeasureTasksTheme {
        CalculateResultView(
            calculateResult = { "1000" }
        )
    }
}
