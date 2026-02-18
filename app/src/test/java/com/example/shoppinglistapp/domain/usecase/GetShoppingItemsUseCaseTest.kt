package com.example.shoppinglistapp.domain.usecase

import com.example.shoppinglistapp.data.model.Category
import com.example.shoppinglistapp.data.model.ShoppingItem
import com.example.shoppinglistapp.data.repository.ShoppingRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetShoppingItemsUseCaseTest {

    private val repository: ShoppingRepository = mockk(relaxed = true)
    private lateinit var getShoppingItemsUseCase: GetShoppingItemsUseCase

    @Before
    fun setUp() {
        getShoppingItemsUseCase = GetShoppingItemsUseCase(repository)
    }

    @Test
    fun `invoke should return flow of items from repository`() = runTest {
        val items = listOf(
            ShoppingItem(id = 1, name = "Milk", category = Category.Milk),
            ShoppingItem(id = 2, name = "Bread", category = Category.Breads)
        )
        coEvery { repository.getAllItems() } returns flowOf(items)

        getShoppingItemsUseCase().collect { result ->
            assertEquals(items, result)
        }
        coVerify(exactly = 1) { repository.getAllItems() }
    }
}
