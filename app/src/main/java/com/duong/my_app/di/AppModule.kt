package com.duong.my_app.di

import android.content.Context
import com.duong.my_app.data.database.MyThemeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by duong_tt on 8/30/2023.
 * Email: tranthaiduong.kailoren@gmail.com
 * Github: https://github.com/thaiduong1995
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRepository(
        @ApplicationContext context: Context,
    ): MyThemeRepository {
        return MyThemeRepository(context)
    }
}