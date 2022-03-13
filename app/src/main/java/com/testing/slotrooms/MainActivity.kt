package com.testing.slotrooms

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import com.testing.slotrooms.presentation.MainScreen
import com.testing.slotrooms.ui.theme.MainBackground
import com.testing.slotrooms.ui.theme.SlotRoomsTheme
import dagger.hilt.android.AndroidEntryPoint

//class MainActivity : ComponentActivity() {
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            SlotRoomsTheme {
                Surface(color = MainBackground) {
                    MainScreen()

                }
            }
        }
    }
}

/*
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
}*/
