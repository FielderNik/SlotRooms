package com.testing.slotrooms.presentation.splash

sealed class SplashState {
    object Loading : SplashState()
    object Completed : SplashState()
}

sealed class SplashEvent {

}

sealed class SplashEffect {
    object LoadCompleted : SplashEffect()
}