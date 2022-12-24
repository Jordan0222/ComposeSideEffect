package com.example.composesideeffect

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlinx.coroutines.delay

@Composable
fun RememberUpdatedState() {
    var buttonColour by remember {
        mutableStateOf("Unknown")
    }
    Column {
        Button(
            onClick = {
                buttonColour = "Blue"
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Blue
            )
        ) {
            Text("Blue Button")
        }
        Spacer(Modifier.height(24.dp))
        Button(
            onClick = {
                buttonColour = "Green"
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Green
            )
        ) {
            Text("Green Button")
        }
        Timer(buttonColor = buttonColour)
    }
}

@Composable
fun Timer(buttonColor: String) {
    val timerDuration = 5000L
    val buttonColorUpdated by rememberUpdatedState(newValue = buttonColor)
    println("RememberUpdatedState Composing timer with colour : $buttonColor")

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner, effect = {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    println("RememberUpdatedState Timer ON_CREATE")
                }
                Lifecycle.Event.ON_RESUME -> {
                    println("RememberUpdatedState Timer ON_RESUME")
                }
                Lifecycle.Event.ON_PAUSE -> {
                    println("RememberUpdatedState Timer ON_PAUSE")
                }
                Lifecycle.Event.ON_DESTROY -> {
                    println("RememberUpdatedState Timer ON_DESTROY")
                }
                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    })

    LaunchedEffect(key1 = Unit, block = {
        println("RememberUpdatedState start in LaunchedEffect")
        startTimer(timerDuration) {
            println("RememberUpdatedState Timer ended")
            println("[1] RememberUpdatedState Last pressed button color was $buttonColor")
            println("[2] RememberUpdatedState Last pressed button color was $buttonColorUpdated")
        }
    })
}

suspend fun startTimer(time: Long, onTimerEnd: () -> Unit) {
    delay(timeMillis = time)
    onTimerEnd()
}