package com.example.shoppinglistapp.data.repository

import com.example.shoppinglistapp.data.local.ShoppingItemDao
import com.example.shoppinglistapp.data.model.ShoppingItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ShoppingRepositoryImpl @Inject constructor(
    private val shoppingItemDao: ShoppingItemDao
) : ShoppingRepository {

    override fun getAllItems(): Flow<List<ShoppingItem>> {
        return shoppingItemDao.getAllItems()
    }

    override suspend fun insert(item: ShoppingItem) {
        shoppingItemDao.insert(item)
    }

    override suspend fun update(item: ShoppingItem) {
        shoppingItemDao.update(item)
    }

    override suspend fun delete(item: ShoppingItem) {
        shoppingItemDao.delete(item)
    }
}