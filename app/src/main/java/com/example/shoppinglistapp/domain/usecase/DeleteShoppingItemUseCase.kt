package com.example.shoppinglistapp.domain.usecase

import com.example.shoppinglistapp.data.model.ShoppingItem
import com.example.shoppinglistapp.data.repository.ShoppingRepository
import javax.inject.Inject

class DeleteShoppingItemUseCase @Inject constructor(
    private val repository: ShoppingRepository
) {
    suspend operator fun invoke(item: ShoppingItem) {
        repository.delete(item)
    }
}
