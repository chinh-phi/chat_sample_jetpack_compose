package com.chinhph.chatsample.domain.models

data class Conversation(
    val id: String,
    val userSend: User,
    val userReceive: User,
    val lastMessage: Message
)