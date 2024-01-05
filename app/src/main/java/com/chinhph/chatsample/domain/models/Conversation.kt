package com.chinhph.chatsample.domain.models

data class Conversation(
    val id: String? = null,
    val lastMessage: Message? = null,
    val members: List<User>? = null,
    val memberIds: List<String>? = null,
)