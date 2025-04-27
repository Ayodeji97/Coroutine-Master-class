package com.plcoding.coroutinesmasterclass.sections.flow_fundamentals.assignment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CountDownApp(
    modifier: Modifier = Modifier,
    viewModel: CountDownViewModel = viewModel(),
) {

    val countDown by viewModel.countDown.collectAsStateWithLifecycle()
    val counter by viewModel.counterFieldValue.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = countDown.toString())

        Spacer(Modifier.height(16.dp))

        TextField(
            value = counter,
            onValueChange = viewModel::onValueChange,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            )
        )

        Spacer(Modifier.height(16.dp))

        Button(onClick = viewModel::startCountDown) {
            Text(text = "Start Countdown")
        }
    }
}


//@Preview
//@Composable
//private fun CountDownPreview() {
//    CountDownApp()
//}