package com.dpudov.livepictures.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.dpudov.livepictures.presentation.ui.screen.MainScreen
import com.dpudov.livepictures.presentation.ui.theme.LivePicturesTheme
import com.dpudov.livepictures.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
//    private val expViewModel: DrawingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            LivePicturesTheme {
                MainScreen(viewModel = viewModel)
            }
        }
    }
}
