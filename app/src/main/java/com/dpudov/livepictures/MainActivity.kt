package com.dpudov.livepictures

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.dpudov.livepictures.ui.theme.LivePicturesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            LivePicturesTheme {
                MainScreen()
            }
        }
    }
}
