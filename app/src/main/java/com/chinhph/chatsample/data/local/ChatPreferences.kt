package com.chinhph.chatsample.data.local

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ChatPreferences @Inject constructor(
    @ApplicationContext context: Context
) : BasePreferences(context, PREF_NAME), ChatLocalConfig {
    override fun setUserId(userId: String) {
        setString(KEY_USER_ID, userId)
    }

    override fun getUserId(): String {
        return getString(KEY_USER_ID).orEmpty()
    }

    companion object {
        private const val PREF_NAME = "PREF_CHAT"
        private const val KEY_USER_ID = "KEY_USER_ID"
    }
}