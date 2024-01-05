package com.chinhph.chatsample.domain.repository

import com.chinhph.chatsample.domain.models.Message
import com.chinhph.chatsample.utils.Response
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun addNewMessage(newMessage: Message): Flow<Response<Boolean>>

    suspend fun getListMessageOfConversation(conversationId: String): Flow<Response<List<Message>>>

    suspend fun updateMessage(messageId: String, newMessage: Message): Flow<Response<Boolean>>
}