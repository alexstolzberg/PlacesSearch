package com.stolz.placessearch.di

import android.app.Application
import androidx.room.Room
import com.stolz.placessearch.database.FavoriteDatabase
import com.stolz.placessearch.database.PlaceDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    internal fun provideFavoriteDatabase(application: Application): FavoriteDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            FavoriteDatabase::class.java,
            "favorites_database"
        )
            .build()
    }

    @Provides
    @Singleton
    internal fun providePlaceDao(favoriteDatabase: FavoriteDatabase): PlaceDao {
        return favoriteDatabase.placeDao()
    }
}