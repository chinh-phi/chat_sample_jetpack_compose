package com.chinhph.chatsample.domain.models

data class Message(
    val id: String,
    val message: String,
    val sendAt: String,
    val updatedAt: String,
    val sendBy: User,
    val replyMessageId: String? = null,
    val state: MessageState = MessageState.SENT
)

enum class MessageState {
    SEEN, SENT, RECEIVED
}