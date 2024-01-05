package com.chinhph.chatsample.data.repository

import com.chinhph.chatsample.data.local.ChatLocalConfig
import javax.inject.Inject

class ChatConfigRepository @Inject constructor(
    private val config: ChatLocalConfig
) {
    fun setUserId(userId: String) = config.setUserId(userId)

    fun getUserId() = config.getUserId()
}