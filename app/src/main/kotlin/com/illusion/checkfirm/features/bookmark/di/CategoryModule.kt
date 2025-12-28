package com.illusion.checkfirm.features.bookmark.di

import android.content.Context
import com.illusion.checkfirm.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object CategoryModule {

    @Provides
    @Named("allString")
    fun provideAllString(@ApplicationContext context: Context): String {
        return context.getString(R.string.category_all)
    }
}