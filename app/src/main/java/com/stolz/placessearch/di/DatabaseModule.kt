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
            application, FavoriteDatabase::class.java, "Favorites.db"
        )
            .allowMainThreadQueries()
            .build()
    }


    @Provides
    @Singleton
    internal fun providePlaceDao(favoriteDatabase: FavoriteDatabase): PlaceDao {
        return favoriteDatabase.placeDao()
    }
}