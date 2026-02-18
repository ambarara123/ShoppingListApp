package com.example.shoppinglistapp.di

import android.content.Context
import androidx.room.Room
import com.example.shoppinglistapp.data.local.ShoppingItemDao
import com.example.shoppinglistapp.data.local.ShoppingItemDatabase
import com.example.shoppinglistapp.data.repository.ShoppingRepository
import com.example.shoppinglistapp.data.repository.ShoppingRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideShoppingItemDatabase(@ApplicationContext context: Context): ShoppingItemDatabase {
        return Room.databaseBuilder(
            context,
            ShoppingItemDatabase::class.java,
            "shopping_item_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideShoppingItemDao(database: ShoppingItemDatabase): ShoppingItemDao {
        return database.shoppingItemDao()
    }

    @Provides
    @Singleton
    fun provideShoppingRepository(dao: ShoppingItemDao): ShoppingRepository {
        return ShoppingRepositoryImpl(dao)
    }
}