package com.example.shoppinglistapp.data.local

import androidx.room.TypeConverter
import com.example.shoppinglistapp.data.model.Category

class RoomTypeConverter {
    @TypeConverter
    fun fromCategory(category: Category): String {
        return category.name
    }

    @TypeConverter
    fun toCategory(category: String): Category {
        return Category.valueOf(category)
    }
}