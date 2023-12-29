package com.chinhph.chatsample.navigation

sealed class Screens(val route: String) {
    object HomeScreen : Screens("home screen")
    object ConversationScreen : Screens("conversation screen")
}