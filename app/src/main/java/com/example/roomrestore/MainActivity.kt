package com.example.roomrestore

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.roomrestore.ui.theme.RoomRestoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainViewModel = viewModel(
                this,
                "MainViewModel",
                MainViewModelFactory(LocalContext.current.applicationContext as Application)
            )
            RoomRestoreTheme {
                Surface(color = MaterialTheme.colors.background) {
                    ScreenSetup(viewModel = viewModel)
                }
            }
        }
    }
}

