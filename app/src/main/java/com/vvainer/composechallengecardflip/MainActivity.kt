package com.vvainer.composechallengecardflip

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbCloudy
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.*
import java.lang.Float.min

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeChallengeCardFlipTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    CardFlipper()
                }
            }
        }
    }
}

@Composable
fun Card(modifier: Modifier = Modifier, iconId: Int, angle : Float) {
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
                painter = painterResource(id = iconId),
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
                    text = "10th Street".uppercase(),
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
                            text = "$angle", color = Color.White, fontSize = 80.sp,
                            textAlign = TextAlign.Center, fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "ft", color = Color.White, fontSize = 20.sp,
                            textAlign = TextAlign.Center, fontWeight = FontWeight.Bold
                        )

                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.WbSunny, contentDescription = null, tint = Color.White)
                        Text("65 °", color = Color.White)
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
                        text = "16.56 mph NE".uppercase(),
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
            Card(Modifier, R.drawable.card1,0f)
        }
    }
}



@Composable
fun CardFlipper() {
    var scrollPct by remember { mutableStateOf(0f) }
    val cardCount = 3
    val maxOffset : Float = cardCount * 1000f
    val iconIds = arrayListOf(R.drawable.card1, R.drawable.card2, R.drawable.card3)
    var offset by remember { mutableStateOf(0f)}
    BoxWithConstraints(modifier = Modifier
        .fillMaxSize()
        .scrollable(
            orientation = Orientation.Horizontal,
            state = rememberScrollableState { delta ->
                val d = -delta
                val mDelta = if (d > 0) min(d, maxOffset - offset) else -min(offset, -d)
                offset += mDelta
                scrollPct = offset / maxOffset
                delta
            }
        )) {
        val halfWidth : Float = (maxWidth.value/2f)
        val cardScrollPct = scrollPct / (1f / (cardCount-1).toFloat())
        for (i in 0 until cardCount) {
            val posX = (i)*maxWidth.value
            val offsetX = posX-(maxWidth.value*cardScrollPct)
            val angle = (offsetX.rem(maxWidth.value) / halfWidth)*(-10f)
            Log.e("CARDFLIP","Card:$i, scrlPct:$cardScrollPct, posX:$posX, maxW:$maxWidth, offset:$offsetX")
            Card(modifier = Modifier.offset(x = offsetX.dp), iconId = iconIds[i.rem(3)], angle=angle)
        }
        Text(text = "$offset, $scrollPct, $cardScrollPct",fontSize = 20.sp, color = Color.White, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true, name = "Card Flipper")
@Composable
fun CardFlipperPreview() {
    ComposeChallengeCardFlipTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Black
        ) {
            CardFlipper()
        }
    }
}