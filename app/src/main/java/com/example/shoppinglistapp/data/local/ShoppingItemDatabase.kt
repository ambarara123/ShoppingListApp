package com.example.shoppinglistapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.shoppinglistapp.data.model.ShoppingItem

@Database(entities = [ShoppingItem::class], version = 1, exportSchema = false)
@TypeConverters(RoomTypeConverter::class)
abstract class ShoppingItemDatabase : RoomDatabase() {
    abstract fun shoppingItemDao(): ShoppingItemDao
}