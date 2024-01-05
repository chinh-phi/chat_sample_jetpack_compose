package com.chinhph.chatsample.data.local

interface ChatLocalConfig {
    fun setUserId(userId: String)

    fun getUserId(): String
}