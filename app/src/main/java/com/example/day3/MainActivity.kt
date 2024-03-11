package com.example.day3

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TimerApp()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerApp() {
    var time by remember { mutableStateOf(25 * 60) }
    var isRunning by remember { mutableStateOf(false) }
    var notificationShown by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val timerNotification = remember { TimerNotification(context) }

    LaunchedEffect(key1 = isRunning, key2 = time) {
        if (isRunning) {
            var localTime = time
            while (localTime > 0) {
                delay(1000)
                localTime--
                withContext(Dispatchers.Main) {
                    time = localTime
                }
            }
            isRunning = false
            if (!notificationShown) {
                notificationShown = true
                timerNotification.showNotification()
                playAlertSound(context)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Timer") },
                actions = {
                    Row {
                        Button(
                            onClick = { time = 25 * 60 },
                            enabled = !isRunning,
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier.padding(horizontal = 4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                        ) {
                            Text(
                                text = "25 min",
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                        Button(
                            onClick = { time = 5 * 60 },
                            enabled = !isRunning,
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier.padding(horizontal = 4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                        ) {
                            Text(
                                text = "5 min",
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                        Button(
                            onClick = { time = 15 * 60 },
                            enabled = !isRunning,
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier.padding(horizontal = 4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                        ) {
                            Text(
                                text = "15 min",
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = formatTime(time),
                    fontSize = 50.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 16.dp)
                )

                Row {
                    Button(
                        onClick = {
                            if (!isRunning) {
                                isRunning = true
                                startTimer(time) {
                                    isRunning = false
                                    if (!notificationShown) {
                                        notificationShown = true
                                        timerNotification.showNotification()
                                        playAlertSound(context)
                                    }
                                }
                            }
                        },
                        enabled = !isRunning
                    ) {
                        Text(text = "Start")
                    }

                    Button(
                        onClick = {
                            isRunning = false
                            notificationShown = false
                        },
                        enabled = isRunning
                    ) {
                        Text(text = "Stop")
                    }
                }
            }
        }
    }
}


fun formatTime(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format(Locale.getDefault(), "%02d:%02d", minutes, remainingSeconds)
}

@OptIn(DelicateCoroutinesApi::class)
fun startTimer(initialTime: Int, onFinish: () -> Unit) {
    GlobalScope.launch {
        var time = initialTime
        while (time > 0) {
            delay(1000)
            time--
            // Update the state variable within the composable
            withContext(Dispatchers.Main) {
                time = time // Update the composable state
            }
        }
        onFinish()
    }
}


fun playAlertSound(context: android.content.Context) {
    val alertSound = android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_ALARM)
    val ringtone = android.media.RingtoneManager.getRingtone(context, alertSound)
    ringtone.play()
}