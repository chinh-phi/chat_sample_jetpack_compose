package com.chinhph.chatsample.ui.screens.authentication.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import com.chinhph.chatsample.utils.Constants.AUTH_SCREEN

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthTopBar() {
    TopAppBar(
        title = {
            Text(
                text = AUTH_SCREEN
            )
        }
    )
}