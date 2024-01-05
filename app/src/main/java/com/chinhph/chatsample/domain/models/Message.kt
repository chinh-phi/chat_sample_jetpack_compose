package com.chinhph.chatsample.domain.models

data class Message(
    val id: String? = null,
    val conversationId: String? = null,
    val message: String? = null,
    val sendAt: Long? = null,
    val updatedAt: String? = null,
    val sendBy: String? = null,
    val replyMessageId: String? = null,
    val state: MessageState = MessageState.SENT,
    val previousMessageId: String? = null,
    val nextMessageId: String? = null,
)

enum class MessageState {
    SEEN, SENT, RECEIVED
}