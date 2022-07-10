package com.testing.slotrooms

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.ExperimentalMaterialApi
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.testing.slotrooms.presentation.MainScreen
import com.testing.slotrooms.ui.theme.SlotRoomsTheme
import com.testing.slotrooms.utils.managers.NetworkConnectionManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

//class MainActivity : ComponentActivity() {
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    @Inject lateinit var connectionManager: NetworkConnectionManager


    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SlotRoomsTheme {
                MainScreen()
            }
        }

        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                connectionManager.networkStatus.collect {
                    Log.d("milk", "networkStatus: $it")
                }
            }
        }
    }
}


