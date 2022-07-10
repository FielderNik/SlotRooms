package com.testing.slotrooms.presentation.splash

sealed class SplashState {
    object Loading : SplashState()
    object Completed : SplashState()
    data class ServerUnavailable(val exception: Exception) : SplashState()
}

sealed class SplashEvent {

}

sealed class SplashEffect {
    object LoadCompleted : SplashEffect()

    sealed class Failed(open val exception: Exception) : SplashEffect() {
        data class UsualFailed(override val exception: Exception) : Failed(exception = exception)
        data class ServerUnavailable(override val exception: Exception) : Failed(exception = exception)
    }

}