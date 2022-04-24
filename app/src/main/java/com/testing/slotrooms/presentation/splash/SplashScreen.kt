package com.testing.slotrooms.presentation.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.testing.slotrooms.R
import com.testing.slotrooms.presentation.Screens
import com.testing.slotrooms.presentation.views.AppScreenState
import com.testing.slotrooms.ui.theme.LocalScreenState

@Composable
fun SplashScreen(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    viewModel: SplashViewModel = hiltViewModel()
) {

    val state = viewModel.state.collectAsState()
    val appScreenState = LocalScreenState.current

    LaunchedEffect(Unit) {
        setupAppScreenState(appScreenState)
    }

    LaunchedEffect(Unit) {
        viewModel.initialization()
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            handleEffect(
                effect = it,
                navController = navController
            )
        }
    }

    SplashScreenContent(state = state.value, navController = navController)

}

@Composable
private fun SplashScreenContent(state: SplashState, navController: NavHostController) {
    when(state) {
        is SplashState.Loading -> {
            LoadingContent()
        }
        is SplashState.Completed -> {
            CompletedContent()
        }
    }
}

private fun setupAppScreenState(appScreenState: AppScreenState) {
    appScreenState.provideEmptyScreen()
}

@Composable
private fun CompletedContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "COMPLETED")
    }
}

@Composable
private fun LoadingContent() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash_slots))

    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
}

private suspend fun handleEffect(
    effect: SplashEffect?,
    navController: NavHostController
) {
    when(effect) {
        is SplashEffect.LoadCompleted -> {
            navController.navigate(Screens.Slots.screenRoute)
        }
    }
}