package com.chinhph.chatsample.ui.screens.conversation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chinhph.chatsample.domain.models.Message
import com.chinhph.chatsample.domain.repository.MessageRepository
import com.chinhph.chatsample.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val messageRepository: MessageRepository
) : ViewModel() {
    private val _messages = MutableStateFlow<Response<List<Message>>?>(null)
    val messages: StateFlow<Response<List<Message>>?>
        get() = _messages

    fun getListMessage(conversationId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                messageRepository.getListMessageOfConversation(conversationId).collect { result ->
                    _messages.value = result
                }
            }
        }
    }

    fun sendMessage(newMessage: Message) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                messageRepository.addNewMessage(newMessage).collect { result ->
                }
            }
        }
    }
}