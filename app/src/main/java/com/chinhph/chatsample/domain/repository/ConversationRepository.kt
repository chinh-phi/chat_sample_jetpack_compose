package com.chinhph.chatsample.domain.repository

import com.chinhph.chatsample.domain.models.Conversation
import com.chinhph.chatsample.utils.Response
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {
    suspend fun getListConversationByUserId(userId: String): Flow<Response<List<Conversation>?>>

    suspend fun createConversation(conversation: Conversation): Flow<Response<Boolean>>

//    suspend fun updateConversationById(conversationId: String)
}