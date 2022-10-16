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
            val mContext = LocalContext.current
            val viewModel: MainViewModel = viewModel(
                this,
                "MainViewModel",
                MainViewModelFactory(mContext.applicationContext as Application)
            )
            val list = viewModel.getAllItem().observeAsState(listOf())
            RoomRestoreTheme {
                Surface(color = MaterialTheme.colors.background) {
                    ScreenSetup(list = list, viewModel = viewModel, context = mContext)
                }
            }
        }
    }
}

