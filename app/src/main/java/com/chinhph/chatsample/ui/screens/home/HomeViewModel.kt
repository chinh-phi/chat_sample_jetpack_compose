package com.chinhph.chatsample.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chinhph.chatsample.data.repository.ChatConfigRepository
import com.chinhph.chatsample.domain.models.Conversation
import com.chinhph.chatsample.domain.repository.ConversationRepository
import com.chinhph.chatsample.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val conversationRepository: ConversationRepository,
    private val configRepository: ChatConfigRepository
) : ViewModel() {
    private val _conversations = MutableStateFlow<Response<List<Conversation>?>?>(null)
    val conversations: StateFlow<Response<List<Conversation>?>?>
        get() = _conversations


    init {
        getListConversation()
    }

    fun getListConversation() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                conversationRepository.getListConversationByUserId(configRepository.getUserId())
                    .collect { result ->
                        _conversations.value = result
                    }
            }
        }
    }

    fun createConversation(conversation: Conversation) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                conversationRepository.createConversation(conversation).collect {

                }
            }
        }
    }

    fun getUserId() = configRepository.getUserId()
}