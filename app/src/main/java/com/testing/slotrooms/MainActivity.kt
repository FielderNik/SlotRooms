package com.testing.slotrooms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.testing.slotrooms.presentation.slots.SlotsScreen
import com.testing.slotrooms.presentation.slots.SlotsViewModel
import com.testing.slotrooms.ui.theme.MainBackground
import com.testing.slotrooms.ui.theme.SlotRoomsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SlotRoomsTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MainBackground) {
                    val slotsViewModel = viewModel<SlotsViewModel>()
                    SlotsScreen(viewModel = slotsViewModel)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SlotRoomsTheme {
//        Greeting(name = "WORLD!")
        val slotsViewModel = viewModel<SlotsViewModel>()
        SlotsScreen(viewModel = slotsViewModel)
    }
}