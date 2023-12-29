package com.chinhph.chatsample.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.chinhph.chatsample.ui.screens.conversation.ConversationScreen
import com.chinhph.chatsample.ui.screens.home.HomeScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screens.HomeScreen.route) {
        composable(Screens.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(Screens.ConversationScreen.route) {
            ConversationScreen(navController = navController)
        }
    }
}