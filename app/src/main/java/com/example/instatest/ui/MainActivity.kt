package com.example.instatest.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.instatest.ui.navigation.AppNavigationGraph
import com.example.instatest.ui.theme.InstaTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InstaTestTheme {
                AppNavigationGraph()
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    InstaTestTheme {
        AppNavigationGraph()
    }
}