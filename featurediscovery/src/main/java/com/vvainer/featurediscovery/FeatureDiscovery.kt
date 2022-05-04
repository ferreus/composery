package com.vvainer.featurediscovery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vvainer.featurediscovery.ui.theme.ComposeryTheme

class FeatureDiscovery : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeryTheme {
                Main()
            }
        }
    }
}

@Composable
fun Main() {
    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(text = "Feature Discovery")
            },

            navigationIcon = {
                // show drawer icon
                IconButton(
                    onClick = {}
                ) {
                    Icon(Icons.Filled.Menu, contentDescription = "")
                }
            },
            actions = {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = "",
                    )
                }
            },
            backgroundColor = Color.Green.copy(green = 0.5f),
            contentColor = Color.White
        )},
        content = {
            Body()
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Default.Add, contentDescription = "")
            }
        }
    )
}

@Composable
fun Body() {
    Column {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("https://www.caffeineinformer.com/wp-content/uploads/starbucks-image.jpg")
                .crossfade(true).build(),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.FillWidth
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.primary)
                    .padding(16.dp)
            ) {
                Text(
                    "Starbucks Coffee",
                    fontSize = 24.sp,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text("Coffee Shop", fontSize = 16.sp, color = Color.White)
            }
            FloatingActionButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.align(alignment = Alignment.CenterEnd).offset(
                    x = (-16).dp,
                    y = (-48).dp
                ),
                backgroundColor = Color.White,
                contentColor = MaterialTheme.colors.primary
            ) {
                Icon(Icons.Default.DriveEta, contentDescription = "")
            }
        }
        OutlinedButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onClick = { /*TODO*/ }) {
            Text("Do Feature Discovery")
        }
    }
}

