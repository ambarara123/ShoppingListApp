package com.example.shoppinglistapp.data.repository

import com.example.shoppinglistapp.data.local.ShoppingItemDao
import com.example.shoppinglistapp.data.model.Category
import com.example.shoppinglistapp.data.model.ShoppingItem
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ShoppingRepositoryImplTest {

    private val shoppingItemDao: ShoppingItemDao = mockk(relaxed = true)
    private lateinit var repository: ShoppingRepositoryImpl

    @Before
    fun setUp() {
        repository = ShoppingRepositoryImpl(shoppingItemDao)
    }

    @Test
    fun `getAllItems should return flow from dao`() = runTest {
        val items = listOf(
            ShoppingItem(id = 1, name = "Milk", category = Category.Milk),
            ShoppingItem(id = 2, name = "Bread", category = Category.Breads)
        )
        coEvery { shoppingItemDao.getAllItems() } returns flowOf(items)

        repository.getAllItems().collect { result ->
            assertEquals(items, result)
        }
        coVerify(exactly = 1) { shoppingItemDao.getAllItems() }
    }

    @Test
    fun `insert should call dao insert`() = runTest {
        val item = ShoppingItem(name = "Eggs", category = Category.Milk)
        repository.insert(item)
        coVerify(exactly = 1) { shoppingItemDao.insert(item) }
    }

    @Test
    fun `update should call dao update`() = runTest {
        val item = ShoppingItem(id = 1, name = "Cheese", category = Category.Milk)
        repository.update(item)
        coVerify(exactly = 1) { shoppingItemDao.update(item) }
    }

    @Test
    fun `delete should call dao delete`() = runTest {
        val item = ShoppingItem(id = 1, name = "Yogurt", category = Category.Milk)
        repository.delete(item)
        coVerify(exactly = 1) { shoppingItemDao.delete(item) }
    }
}
