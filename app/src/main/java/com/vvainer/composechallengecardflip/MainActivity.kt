package com.vvainer.composechallengecardflip

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vvainer.composechallengecardflip.ui.theme.ComposeChallengeCardFlipTheme
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.launch
import java.lang.Float.min
import kotlin.math.roundToInt


data class CardInfo(val street: String, val mainLabel: String, val temp: Int, val wind: String, val icon: Int)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cards = arrayListOf(
            CardInfo("Villefranche-sur-Mer", "0-2", 13, "11 km/h SW", R.drawable.card1),
            CardInfo("Conciergerie", "6-3", 6, "10 km/h E", R.drawable.card2),
            CardInfo("Chalet Topaze", "7-12", -3, "4 km/h W", R.drawable.card3),
        )
        setContent {
            ComposeChallengeCardFlipTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    CardFlipper(cards)
                }
            }
        }
    }
}

@Composable
fun Card(modifier: Modifier = Modifier, model: CardInfo, angle : Float) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier
            .fillMaxHeight()
            .padding(36.dp).graphicsLayer {
                rotationY = angle
            }
    ) {
        Box(
            modifier = Modifier
                .background(Color.Black)
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = model.icon),
                contentDescription = null,
                contentScale = ContentScale.FillHeight, modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = model.street.uppercase(),
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                Column() {
                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = model.mainLabel, color = Color.White, fontSize = 80.sp,
                            textAlign = TextAlign.Center, fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "ft", color = Color.White, fontSize = 20.sp,
                            textAlign = TextAlign.Center, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp)
                        )

                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.WbSunny, contentDescription = null, tint = Color.White)
                        Text("${model.temp} °", color = Color.White)
                    }
                }
                Row(modifier = Modifier
                    .border(1.dp, Color.White, CircleShape)
                    .background(SolidColor(Color.Black.copy(alpha = 0.2f)), shape = CircleShape)
                    .padding(16.dp)
                ) {
                    Text(
                        text = "Mostly Cloudy".uppercase(),
                        color = Color.White,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(Icons.Default.WbCloudy, tint = Color.White, contentDescription = null,
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp))
                    Text(
                        text = model.wind.uppercase(),
                        color = Color.White,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardPreview() {
    ComposeChallengeCardFlipTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black
        ) {
            Card(Modifier, CardInfo("bla","3-6",32,"12.3 km/h E",R.drawable.card1),0f)
        }
    }
}



@Composable
fun CardFlipper(cards: ArrayList<CardInfo>) {
    val currentAnimValue = remember { Animatable(0.0f) }
    val coroutineScope = rememberCoroutineScope()
    var dragging by remember { mutableStateOf(false)}
    var scrollPct by remember { mutableStateOf(0f) }

    val cardCount = cards.size
    val maxOffset : Float = (cardCount-1) * 1000f
    val iconIds = arrayListOf(R.drawable.card1, R.drawable.card2, R.drawable.card3)
    var offset by remember { mutableStateOf(0f)}
    BoxWithConstraints(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectHorizontalDragGestures(
                onDragStart = {
                    dragging = true
                },
                onDragEnd = {
                    coroutineScope.launch {
                        dragging = false
                        currentAnimValue.snapTo(scrollPct)
                        val targetPct = ((scrollPct / (1.0/(cardCount-1))).roundToInt().toFloat() / (cardCount-1)).coerceIn(0f,1f)
                        currentAnimValue.animateTo(targetPct)
                        scrollPct = targetPct
                        offset = scrollPct*maxOffset

                    }
                }
            ) { change, dragAmount ->
                change.consumeAllChanges()
                offset = (offset-dragAmount).coerceIn(0f ,maxOffset)
                scrollPct = offset / maxOffset
            }
        }
    ) {
        val pct = if (dragging) scrollPct else currentAnimValue.value
        val halfWidth : Float = (maxWidth.value/2f)
        val cardScrollPct = pct / (1f / (cardCount-1).toFloat())
        for (i in 0 until cardCount) {
            val posX = (i)*maxWidth.value
            val offsetX = posX-(maxWidth.value*cardScrollPct)
            val angle = (offsetX.rem(maxWidth.value) / halfWidth)*(-10f)
            Card(modifier = Modifier.offset(x = offsetX.dp), model = cards[i.rem(3)], angle=angle)
        }
    }
}

@Preview(showBackground = true, name = "Card Flipper")
@Composable
fun CardFlipperPreview() {
    val cards = arrayListOf(
        CardInfo("Villefranche-sur-Mer", "0-2", 13, "11 km/h SW", R.drawable.card1),
        CardInfo("Conciergerie", "6-3", 6, "10 km/h E", R.drawable.card2),
        CardInfo("Chalet Topaze", "7-12", -3, "4 km/h W", R.drawable.card3),
    )
    ComposeChallengeCardFlipTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black
        ) {
            CardFlipper(cards)
        }
    }
}