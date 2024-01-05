package com.chinhph.chatsample.ui.screens.authentication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chinhph.chatsample.data.repository.ChatConfigRepository
import com.chinhph.chatsample.domain.models.User
import com.chinhph.chatsample.domain.repository.AuthRepository
import com.chinhph.chatsample.domain.repository.OneTapSignInResponse
import com.chinhph.chatsample.domain.repository.SignInWithGoogleResponse
import com.chinhph.chatsample.utils.Response
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository,
    private val configRepository: ChatConfigRepository,
    val oneTapClient: SignInClient
) : ViewModel() {
    val isUserAuthenticated get() = repo.isUserAuthenticatedInFirebase

    var oneTapSignInResponse by mutableStateOf<OneTapSignInResponse>(Response.Success(null))
        private set
    var signInWithGoogleResponse by mutableStateOf<SignInWithGoogleResponse?>(null)
        private set

    fun oneTapSignIn() = viewModelScope.launch {
        oneTapSignInResponse = Response.Loading
        oneTapSignInResponse = repo.oneTapSignInWithGoogle()
    }

    fun signInWithGoogle(googleCredential: AuthCredential) = viewModelScope.launch {
        oneTapSignInResponse = Response.Loading
        signInWithGoogleResponse = Response.Loading
        signInWithGoogleResponse = repo.firebaseSignInWithGoogle(googleCredential)
        if (signInWithGoogleResponse is Response.Success) {
            configRepository.setUserId((signInWithGoogleResponse as Response.Success<User>).data?.userId.orEmpty())
        }
    }
}