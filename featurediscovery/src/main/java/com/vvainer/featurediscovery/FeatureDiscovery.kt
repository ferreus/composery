package com.vvainer.featurediscovery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

        }
    )
}

