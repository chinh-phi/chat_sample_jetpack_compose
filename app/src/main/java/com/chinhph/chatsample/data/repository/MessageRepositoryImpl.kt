package com.chinhph.chatsample.data.repository

import com.chinhph.chatsample.domain.models.Message
import com.chinhph.chatsample.domain.repository.MessageRepository
import com.chinhph.chatsample.utils.Response
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class MessageRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : MessageRepository {
    override suspend fun addNewMessage(newMessage: Message): Flow<Response<Boolean>> = callbackFlow {
        trySend(Response.Loading)
        val ref = db.collection("messages").document()
        val refId = ref.id
        ref.set(newMessage.copy(id = refId))
            .addOnCompleteListener {
                trySend(Response.Success(true))
            }
            .addOnFailureListener { e ->
                trySend(Response.Failure(e))
            }

        newMessage.previousMessageId?.let {
            db.collection("messages").document(it)
                .update("nextMessageId", refId)
        }

        db.collection("conversations").document(newMessage.conversationId.orEmpty())
            .update("lastMessage", newMessage)

        awaitClose {

        }
    }

    override suspend fun getListMessageOfConversation(conversationId: String): Flow<Response<List<Message>>> = callbackFlow {
        trySend(Response.Loading)
        val doc = db.collection("messages").whereEqualTo("conversationId", conversationId)
        val sub = doc.addSnapshotListener { documents, error ->
            val result: MutableList<Message> = mutableListOf()
            if (error != null) {
                trySend(Response.Failure(error))
                close()
                return@addSnapshotListener
            }
            if (documents != null) {
                for (document in documents) {
                    result.add(document.toObject(Message::class.java))
                }
            }
            trySend(Response.Success(result.toList().sortedBy { it.sendAt }))
        }
        awaitClose { sub.remove() }
    }

    override suspend fun updateMessage(
        messageId: String,
        newMessage: Message
    ): Flow<Response<Boolean>> = callbackFlow {
        trySend(Response.Loading)
        val ref = db.collection("messages").document(messageId)
        ref.set(newMessage)
            .addOnCompleteListener {
                trySend(Response.Success(true))
            }
            .addOnFailureListener { e ->
                trySend(Response.Failure(e))
            }
        awaitClose {

        }
    }
}