package com.vvainer.eggtimer

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.graphics.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.Paragraph
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vvainer.eggtimer.ui.theme.ComposeChallengeCardFlipTheme
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

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
    var currentTimeInSeconds by remember {
        mutableStateOf(0)
    }
    Column(modifier = Modifier.fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
        TimerLabel(currentTimeInSeconds)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(30.dp)
        ) {
            TimerDialerInteractive(0,
                Modifier.fillMaxSize()
            ) {
                currentTimeInSeconds = it
            }
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
fun TimerLabel(currentTimeInSeconds: Int) {
    val minutes = currentTimeInSeconds / 60
    val seconds = currentTimeInSeconds - minutes*60
    Text(
        "%02d:%02d".format(minutes,seconds),
        Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = Color.Black,
        letterSpacing = 10.sp,
        style = MaterialTheme.typography.h1
    )
}

/*
@Preview("Timer Label")
@Composable
fun TimerLabelPreview() {
    ComposeChallengeCardFlipTheme {
        TimerLabel()
    }
}
*/

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeChallengeCardFlipTheme {
        EggTimerScreen()
    }
}




@Composable
fun TimerDialer(currentTimeInSeconds: Int, modifier : Modifier = Modifier) {
    val maxTimeInSeconds = 30f*60f
    val pct = currentTimeInSeconds / maxTimeInSeconds
    val arrowAngle = (pct*(Math.PI*2-Math.PI*2f/7f)).toDegrees()
    val paddingPx : Float = with(LocalDensity.current) { 65.dp.toPx() }
    BoxWithConstraints(modifier = modifier
        .fillMaxWidth()
        .padding(24.dp)
        .aspectRatio(1f)
        .shadow(8.dp, CircleShape, false)
        .background(Brush.verticalGradient(listOf(gradientTop, gradientBottom)), CircleShape)
        .drawBehind {
            val radius = (size.width / 2f - paddingPx) + 45f
            var angle = -Math.PI / 2f
            val ticks = 35
            val angleStep: Double = ((2 * Math.PI) / ticks)
            for (i in 0 until ticks) {
                val r = if (i.rem(5) == 0) radius + 25f else radius
                val sw = if (i.rem(5) == 0) 5f else 3f
                val sx = center.x + ((radius - 20f) * cos(angle)).toFloat()
                val sy = center.y + ((radius - 20f) * sin(angle)).toFloat()
                val tx = center.x + (r * cos(angle)).toFloat()
                val ty = center.y + (r * sin(angle)).toFloat()
                angle += angleStep
                drawLine(Color.DarkGray, Offset(sx, sy), Offset(tx, ty), strokeWidth = sw)
            }
            angle = -Math.PI / 2f
            val tx = center.x + (radius * cos(angle)).toFloat()
            val ty = center.y + (radius * sin(angle)).toFloat()
            rotate(arrowAngle.toFloat()) {
                val path = Path()
                path.moveTo(tx, ty)
                path.lineTo(tx - 25f, ty + 40f)
                path.lineTo(tx + 25f, ty + 40f)
                path.close()
                drawPath(path, Color.Black, style = Fill)
                drawOutline(
                    Outline.Generic(path),
                    Brush.horizontalGradient(listOf(Color.DarkGray, Color.LightGray)),
                    style = Stroke(width = 1f)
                )
            }
        }
    ) {

        val angles = listOf(0f, 0f, -90f, 180f, 180f, 90f, 0f)
        for (i in 0..6) {
            Box(
                 modifier = Modifier
                     .fillMaxSize()
                     .align(Alignment.TopCenter)
                     .padding(20.dp)

                     .rotate(i * (360f / 7f))) {

                Text(
                    "${i * 5}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .rotate(angles[i]),
                    textAlign = TextAlign.Center
                )
            }
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(65.dp)
            .aspectRatio(1f)
            .shadow(8.dp, CircleShape, false)
            .background(Brush.verticalGradient(listOf(gradientTop, gradientBottom)), CircleShape)
        ) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .aspectRatio(1f)
                .border(width = 1.5.dp, color = Color(0xFFDFDFDF), CircleShape)) {
                Image(painter = painterResource(id = R.drawable.kotlin),
                    colorFilter = ColorFilter.tint(Color.Black),
                    contentDescription = "kotlin logo", modifier = Modifier
                        .size(48.dp)
                        .align(
                            Alignment.Center
                        )
                        .rotate(arrowAngle))
            }
        }
    }
}


@Composable
fun TimerDialerInteractive(currentTimeInSeconds: Int, modifier : Modifier = Modifier, onTimeChanged: (Int) -> Unit = {}) {
    var currentTime by remember { mutableStateOf(currentTimeInSeconds)}
    var dragPoint = Offset(0f, 0f)
    TimerDialer(currentTimeInSeconds = currentTime,
        modifier = modifier.pointerInput(Unit) {
            detectDragGestures(onDragStart = {
                dragPoint = it
            }, onDragEnd = {

            }) { change, dragAmount ->
                dragPoint += dragAmount
                currentTime = getCurrentTime(dragPoint, size)
                Log.e("DRAG","X:${dragPoint.x}, Y:${dragPoint.y} size:${size.width}, ${size.height}")
                onTimeChanged(currentTime)
            }
        })
}

fun getCurrentTime(dragCenter: Offset, size: IntSize): Int {
    val radius = size.width/2f
    val pos  = Offset(dragCenter.x - radius, dragCenter.y - radius)
    val (dx,dy) = Offset(0f,0f) - pos
    var theta = atan2(dy,dx)
    while (theta<0f) {
        theta+= Math.PI.toFloat()*2f
    }
    while (theta>Math.PI*2) {
        theta -= Math.PI.toFloat()*2f
    }
    theta -= Math.PI.toFloat()/2f
    val pct = theta / (Math.PI*2f)
    val seconds = pct * (45*60)
    Log.e("DRAG","pos:${pos.x},${pos.y} -> dx,dy:$dx,$dy => theta:$theta, pct:$pct, seconds:$seconds")
    return seconds.toInt().coerceIn(0,35*60)
}

private fun Double.toDegrees(): Float {
    return (this*180f/Math.PI).toFloat()
}

@Preview(showBackground = true)
@Composable
fun TimerDialerPreview() {
    ComposeChallengeCardFlipTheme {
        TimerDialer(5)
    }
}