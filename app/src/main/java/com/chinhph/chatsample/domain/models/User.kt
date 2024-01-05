package com.chinhph.chatsample.domain.models

data class User(
    val userId: String? = null,
    val displayName: String? = null,
    val photoUrl: String? = null,
    val nickName: String? = "abc",
    val email: String? = "abc"
)
