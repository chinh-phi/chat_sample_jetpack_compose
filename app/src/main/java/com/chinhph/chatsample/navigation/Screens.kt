package com.chinhph.chatsample.navigation

import com.chinhph.chatsample.utils.Constants.AUTH_SCREEN

sealed class Screens(val route: String) {
    object AuthScreen: Screens(AUTH_SCREEN)
    object HomeScreen : Screens("home screen")
    object ConversationScreen : Screens("conversation screen")
}