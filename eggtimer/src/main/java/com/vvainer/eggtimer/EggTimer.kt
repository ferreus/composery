package com.vvainer.eggtimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vvainer.eggtimer.ui.theme.ComposeChallengeCardFlipTheme

val gradientTop = Color(0xFFF5F5F5)
val gradientBottom = Color(0xFFE8E8E8)

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
    Column(modifier = Modifier.fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
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
        BottomTimerButtons()
    }
}

@Composable
fun BottomTimerButtons() {
    Column() {
        Row() {
            EggTimerButton(title = "Restart", icon = Icons.Default.Refresh) {}
            Spacer(modifier = Modifier.weight(0.2f))
            EggTimerButton(title = "Reset", icon = Icons.Default.ArrowBack) {}
        }
        EggTimerButton("Pause", Icons.Default.Pause, modifier = Modifier.fillMaxWidth()) {}
    }
}

@Composable
fun EggTimerButton(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier
            .background(Color.White)
            .padding(8.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
        onClick = {},
    ) {
        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, tint = Color.Black, contentDescription = title)
            Text(title.uppercase(), color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Bold, letterSpacing = 3.sp)
        }
    }
}

@Composable
fun TimerLabel() {
    Text(
        "15:23",
        Modifier
            .padding(16.dp)
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


@Composable
fun TimerDialer() {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(24.dp)
        .aspectRatio(1f)
        .shadow(8.dp, CircleShape,false)
        .background(Brush.verticalGradient(listOf(gradientTop, gradientBottom)),CircleShape)

    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(65.dp)
            .aspectRatio(1f)
            .shadow(8.dp, CircleShape,false)
            .background(Brush.verticalGradient(listOf(gradientTop, gradientBottom)),CircleShape)) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .aspectRatio(1f)
                .border(width=1.5.dp, color = Color(0xFFDFDFDF))) {

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimerDialerPreview() {
    ComposeChallengeCardFlipTheme {
        TimerDialer()
    }
}