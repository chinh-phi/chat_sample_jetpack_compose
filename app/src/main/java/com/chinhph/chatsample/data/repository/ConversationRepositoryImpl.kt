package com.chinhph.chatsample.data.repository

import com.chinhph.chatsample.domain.models.Conversation
import com.chinhph.chatsample.domain.repository.ConversationRepository
import com.chinhph.chatsample.utils.Response
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ConversationRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
) : ConversationRepository {
    override suspend fun getListConversationByUserId(userId: String): Flow<Response<List<Conversation>?>> =
        callbackFlow {
            trySend(Response.Loading)
            val doc = db.collection("conversations").whereArrayContains("memberIds", userId)
            val sub = doc.addSnapshotListener { documents, error ->
                val result: MutableList<Conversation> = mutableListOf()
                if (error != null) {
                    trySend(Response.Failure(error))
                    close()
                    return@addSnapshotListener
                }
                if (documents != null) {
                    for (document in documents) {
                        result.add(document.toObject(Conversation::class.java))
                    }
                }
                trySend(Response.Success(result.toList()))
            }
            awaitClose { sub.remove() }
        }

    override suspend fun createConversation(conversation: Conversation): Flow<Response<Boolean>> =
        callbackFlow {
            trySend(Response.Loading)
            val ref = db.collection("conversations").document()
            val refId = ref.id
            ref.set(conversation.copy(id = refId))
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