package com.chinhph.chatsample.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.chinhph.chatsample.ui.screens.authentication.AuthScreen
import com.chinhph.chatsample.ui.screens.conversation.ConversationScreen
import com.chinhph.chatsample.ui.screens.home.HomeScreen
import com.chinhph.chatsample.ui.screens.home.HomeViewModel

@Composable
fun NavGraph(navController: NavHostController, homeViewModel: HomeViewModel) {
    NavHost(navController = navController, startDestination = Screens.AuthScreen.route) {
        composable(
            route = Screens.AuthScreen.route
        ) {
            AuthScreen(
                navigateToProfileScreen = {
                    navController.navigate(Screens.HomeScreen.route)
                }
            )
        }
        composable(
            route = Screens.HomeScreen.route
        ) {
            HomeScreen(
                navController = navController,
                onNavIconPressed = {
                    navController.popBackStack()
                    navController.navigate(Screens.AuthScreen.route)
                }
            )
        }
        composable(
            route = Screens.ConversationScreen.route + "/{conversationId}",
            arguments = listOf(
                navArgument("conversationId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            ConversationScreen(
                navController = navController,
                conversationId = backStackEntry.arguments?.getString("conversationId"),
                homeViewModel = homeViewModel
            )
        }
    }
}