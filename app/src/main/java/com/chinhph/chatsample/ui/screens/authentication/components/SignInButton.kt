package com.chinhph.chatsample.ui.screens.authentication.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chinhph.chatsample.R
import com.chinhph.chatsample.utils.Constants.SIGN_IN_WITH_GOOGLE

@Composable
fun SignInButton(
    onClick: () -> Unit
) {
    Button(
        modifier = Modifier.padding(bottom = 48.dp),
        shape = RoundedCornerShape(6.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(R.color.blue400)
        ),
        onClick = onClick
    ) {
        Image(
            painter = painterResource(
                id = R.drawable.ic_app_logo
            ),
            contentDescription = null
        )
        Text(
            text = SIGN_IN_WITH_GOOGLE,
            modifier = Modifier.padding(6.dp),
            fontSize = 18.sp
        )
    }
}