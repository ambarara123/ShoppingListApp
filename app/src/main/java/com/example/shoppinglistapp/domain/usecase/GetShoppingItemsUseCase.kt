package com.example.shoppinglistapp.domain.usecase

import com.example.shoppinglistapp.data.model.ShoppingItem
import com.example.shoppinglistapp.data.repository.ShoppingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetShoppingItemsUseCase @Inject constructor(
    private val repository: ShoppingRepository
) {
    operator fun invoke(): Flow<List<ShoppingItem>> {
        return repository.getAllItems()
    }
}
