package com.example.shoppinglistapp.domain.usecase

import com.example.shoppinglistapp.data.model.Category
import com.example.shoppinglistapp.data.model.ShoppingItem
import com.example.shoppinglistapp.data.repository.ShoppingRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateShoppingItemUseCaseTest {

    private val repository: ShoppingRepository = mockk(relaxed = true)
    private lateinit var updateShoppingItemUseCase: UpdateShoppingItemUseCase

    @Before
    fun setUp() {
        updateShoppingItemUseCase = UpdateShoppingItemUseCase(repository)
    }

    @Test
    fun `invoke should call repository update`() = runTest {
        val item = ShoppingItem(id = 1, name = "Cheese", category = Category.Milk)
        updateShoppingItemUseCase(item)
        coVerify(exactly = 1) { repository.update(item) }
    }
}
