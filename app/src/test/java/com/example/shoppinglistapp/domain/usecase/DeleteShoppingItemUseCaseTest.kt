package com.example.shoppinglistapp.domain.usecase

import com.example.shoppinglistapp.data.model.Category
import com.example.shoppinglistapp.data.model.ShoppingItem
import com.example.shoppinglistapp.data.repository.ShoppingRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteShoppingItemUseCaseTest {

    private val repository: ShoppingRepository = mockk(relaxed = true)
    private lateinit var deleteShoppingItemUseCase: DeleteShoppingItemUseCase

    @Before
    fun setUp() {
        deleteShoppingItemUseCase = DeleteShoppingItemUseCase(repository)
    }

    @Test
    fun `invoke should call repository delete`() = runTest {
        val item = ShoppingItem(id = 1, name = "Yogurt", category = Category.Milk)
        deleteShoppingItemUseCase(item)
        coVerify(exactly = 1) { repository.delete(item) }
    }
}
