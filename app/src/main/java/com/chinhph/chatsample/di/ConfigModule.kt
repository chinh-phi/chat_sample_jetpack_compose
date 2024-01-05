package com.chinhph.chatsample.di

import com.chinhph.chatsample.data.local.ChatLocalConfig
import com.chinhph.chatsample.data.local.ChatPreferences
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@InstallIn(ActivityRetainedComponent::class)
@Module
abstract class ConfigModule {

    @Binds
    abstract fun bindConfig(config: ChatPreferences): ChatLocalConfig
}