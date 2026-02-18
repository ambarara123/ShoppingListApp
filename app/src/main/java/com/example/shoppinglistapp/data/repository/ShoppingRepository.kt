package com.example.shoppinglistapp.data.repository

import com.example.shoppinglistapp.data.model.ShoppingItem
import kotlinx.coroutines.flow.Flow

interface ShoppingRepository {

    fun getAllItems(): Flow<List<ShoppingItem>>

    suspend fun insert(item: ShoppingItem)

    suspend fun update(item: ShoppingItem)

    suspend fun delete(item: ShoppingItem)
}