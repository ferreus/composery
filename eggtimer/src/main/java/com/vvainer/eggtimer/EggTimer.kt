package com.vvainer.eggtimer

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vvainer.eggtimer.ui.theme.ComposeChallengeCardFlipTheme
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

val gradientTop = Color(0xF5F5F5FF)
val gradientBottom = Color(0xE8E8E8FF)

enum class TimerState {
    Setup, Running, Paused
}

class TimerViewModel() : ViewModel() {
    var timer : CountDownTimer? = null

    var timerSetTime by mutableStateOf(0)
        private set
    var timerCurTime by mutableStateOf(0)
        private set

    var state by mutableStateOf(TimerState.Setup)
        private set



    fun setTime(timeInSeconds: Int) {
        timerSetTime = timeInSeconds
        timerCurTime = timeInSeconds
    }
    fun startTimer() {
        if (timerSetTime != 0) {
            state = TimerState.Running
            timer = object : CountDownTimer((timerCurTime * 1000).toLong(),1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timerCurTime = (millisUntilFinished / 1000).toInt()
                }

                override fun onFinish() {
                    state  = TimerState.Setup
                }

            }
            timer?.start()
        }
    }
    fun pauseTimer() {
        if (state == TimerState.Paused) {
            startTimer()
        } else {
            state = TimerState.Paused
            timer?.cancel()
            timer = null
        }
    }

    fun resetTimer() {
        timer?.cancel()
        timer = null
        timerSetTime = 0
        timerCurTime = 0
        state = TimerState.Setup
    }

    fun restartTimer() {
        if (timerSetTime != 0) {
            pauseTimer()
            timerCurTime = timerSetTime
            startTimer()
        }
    }

}



class EggTimer : ComponentActivity() {
    private val model: TimerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeChallengeCardFlipTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    EggTimerScreen(model)
                }
            }
        }
    }
}


@Composable
fun EggTimerScreen(model: TimerViewModel) {
    Column(modifier = Modifier.fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally) {
        TimerLabel(model.timerCurTime)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(30.dp)
        ) {
            TimerDialerInteractive(model.timerCurTime,
                enabled = model.state == TimerState.Setup,
                modifier = Modifier.fillMaxSize(),
                onTimeChanged = {
                    Log.e("VIEWMODEL","Time set to $it")
                    model.setTime(it)
                }
            ) {
                Log.e("VIEWMODEL","Time set to $it, START")
                model.setTime(it)
                model.startTimer()
            }
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f)
        )
        if (model.state != TimerState.Setup) {
            BottomTimerButtons(state = model.state,
                onRestart = {
                    model.restartTimer()
                },
                onReset = {
                    model.resetTimer()
                },
                onPause = {
                    model.pauseTimer()
                }
            )
        }
    }
}

@Composable
fun BottomTimerButtons(state:  TimerState, onRestart: () -> Unit, onReset : () -> Unit, onPause: () -> Unit) {
    Column() {
        Row() {
            EggTimerButton(title = "Restart", icon = Icons.Default.Refresh, onClick = onRestart)
            Spacer(modifier = Modifier.weight(0.2f))
            EggTimerButton(title = "Reset", icon = Icons.Default.ArrowBack, onClick = onReset)
        }
        EggTimerButton(if (state != TimerState.Paused) "Pause" else "Resume", Icons.Default.Pause, modifier = Modifier.fillMaxWidth(), onClick = onPause)
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
        onClick = onClick,
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
        letterSpacing = 3.sp,
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
    val model: TimerViewModel = TimerViewModel()
    ComposeChallengeCardFlipTheme {
        EggTimerScreen(model)
    }
}




@Composable
fun TimerDialer(currentTimeInSeconds: Int, modifier : Modifier = Modifier) {
    val maxTimeInSeconds = 36f*60f
    val pct = currentTimeInSeconds / maxTimeInSeconds
    val arrowAngle = (pct*(Math.PI*2).toDegrees())
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
            val ticks = 36
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
            val angle = (i*5) * (360f / 36f)
            Box(
                 modifier = Modifier
                     .fillMaxSize()
                     .align(Alignment.TopCenter)
                     .padding(20.dp)

                     .rotate(angle)) {

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
fun TimerDialerInteractive(currentTimeInSeconds: Int, modifier : Modifier = Modifier,enabled : Boolean = true, onTimeChanged: (Int) -> Unit = {}, onTimeSet: (Int) -> Unit ) {
    //var currentTime by remember { mutableStateOf(currentTimeInSeconds)}
    var currentTime = currentTimeInSeconds
    val maxTimeInSeconds = 36*60
    var dragPoint = Offset(0f, 0f)
    var startAngle = 0f
    var currentTimeAsAngle = (currentTimeInSeconds / maxTimeInSeconds.toFloat())*(Math.PI.toFloat()*2f)
    TimerDialer(currentTimeInSeconds = currentTime,
        modifier = if (!enabled) Modifier else modifier.pointerInput(Unit) {
            detectDragGestures(onDragStart = {
                dragPoint = it
                startAngle = getRadialAngle(it, size)
            }, onDragEnd = {
                    onTimeSet(currentTime)
            }) { change, dragAmount ->
                change.consumeAllChanges()
                dragPoint += dragAmount
                val curAngle = getRadialAngle(dragPoint, size)
                var deltaAngle = startAngle - curAngle
                if (deltaAngle < 0f) {
                    deltaAngle = (deltaAngle+Math.PI.toFloat()*2f)
                }
                if (deltaAngle > Math.PI) {
                    deltaAngle = Math.PI.toFloat()*2f - deltaAngle
                }
                if ((curAngle - startAngle)<0f) {
                    deltaAngle = -deltaAngle
                }
                currentTimeAsAngle += deltaAngle
                startAngle = curAngle
                currentTime = ((currentTimeAsAngle / (Math.PI.toFloat()*2f))*maxTimeInSeconds).toInt()
                if (currentTime < 0) {
                    currentTime += maxTimeInSeconds
                }
                if (currentTime > maxTimeInSeconds) {
                    currentTime -= maxTimeInSeconds
                }
                onTimeChanged(currentTime)
            }
        })
}

fun getRadialAngle(dragCenter: Offset, size: IntSize): Float {
    val radius = size.width/2f
    val pos  = Offset(dragCenter.x - radius, dragCenter.y - radius)
    val (dx,dy) = Offset(0f,0f) - pos
    var angle = atan2(dy,dx) - Math.PI.toFloat()/2f
    if (angle < 0) {
        angle += (Math.PI*2f).toFloat()
    }
    return angle
}

private fun Double.toDegrees(): Float {
    return (this*180f/Math.PI).toFloat()
}
private fun Float.toDegrees(): Float {
    return (this*180f/Math.PI).toFloat()
}

@Preview(showBackground = true)
@Composable
fun TimerDialerPreview() {
    ComposeChallengeCardFlipTheme {
        TimerDialer(5)
    }
}