package com.chinhph.chatsample.navigation

import com.chinhph.chatsample.utils.Constants.AUTH_SCREEN
import com.chinhph.chatsample.utils.Constants.CONVERSATION_SCREEN
import com.chinhph.chatsample.utils.Constants.HOME_SCREEN

sealed class Screens(val route: String) {
    object AuthScreen: Screens(AUTH_SCREEN)
    object HomeScreen : Screens(HOME_SCREEN)
    object ConversationScreen : Screens(CONVERSATION_SCREEN)
}