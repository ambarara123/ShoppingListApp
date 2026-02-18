package com.example.shoppinglistapp.domain.usecase

import com.example.shoppinglistapp.data.model.Category
import com.example.shoppinglistapp.data.model.ShoppingItem
import com.example.shoppinglistapp.data.repository.ShoppingRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class AddShoppingItemUseCaseTest {

    private val repository: ShoppingRepository = mockk(relaxed = true)
    private lateinit var addShoppingItemUseCase: AddShoppingItemUseCase

    @Before
    fun setUp() {
        addShoppingItemUseCase = AddShoppingItemUseCase(repository)
    }

    @Test
    fun `invoke should call repository insert`() = runTest {
        val item = ShoppingItem(name = "Eggs", category = Category.Milk)
        addShoppingItemUseCase(item)
        coVerify(exactly = 1) { repository.insert(item) }
    }
}
