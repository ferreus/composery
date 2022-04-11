package com.vvainer.eggtimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vvainer.eggtimer.ui.theme.ComposeChallengeCardFlipTheme

class EggTimer : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeChallengeCardFlipTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    EggTimerScreen()
                }
            }
        }
    }
}

@Composable
fun EggTimerScreen() {
    Column(modifier = Modifier.fillMaxHeight()) {
        TimerLabel()
        Box(
            modifier = Modifier
                .background(Color.Blue)
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(30.dp)
        ) {
            Box(
                Modifier
                    .background(Color.DarkGray)
                    .fillMaxSize()
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f)
        )
        Box(
            Modifier
                .background(Color.Cyan)
                .fillMaxWidth()
                .height(100.dp)
        )
        Box(
            Modifier
                .background(Color.Magenta)
                .fillMaxWidth()
                .height(100.dp)
        )
    }
}

@Composable
fun TimerLabel() {
    Text(
        "15:23",
        Modifier.padding(16.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = Color.Black,
        letterSpacing = 10.sp,
        style = MaterialTheme.typography.h1
    )
}

@Preview("Timer Label")
@Composable
fun TimerLabelPreview() {
    ComposeChallengeCardFlipTheme {
        TimerLabel()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeChallengeCardFlipTheme {
        EggTimerScreen()
    }
}