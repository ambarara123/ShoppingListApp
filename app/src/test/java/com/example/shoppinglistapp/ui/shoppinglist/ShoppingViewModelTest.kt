package com.example.shoppinglistapp.ui.shoppinglist

import app.cash.turbine.test
import com.example.shoppinglistapp.data.model.Category
import com.example.shoppinglistapp.data.model.ShoppingItem
import com.example.shoppinglistapp.domain.usecase.AddShoppingItemUseCase
import com.example.shoppinglistapp.domain.usecase.DeleteShoppingItemUseCase
import com.example.shoppinglistapp.domain.usecase.GetShoppingItemsUseCase
import com.example.shoppinglistapp.domain.usecase.UpdateShoppingItemUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ShoppingViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val getShoppingItemsUseCase: GetShoppingItemsUseCase = mockk()
    private val addShoppingItemUseCase: AddShoppingItemUseCase = mockk(relaxed = true)
    private val updateShoppingItemUseCase: UpdateShoppingItemUseCase = mockk(relaxed = true)
    private val deleteShoppingItemUseCase: DeleteShoppingItemUseCase = mockk(relaxed = true)
    private lateinit var viewModel: ShoppingViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onSaveItem should call addShoppingItemUseCase for new item`() = runTest {
        coEvery { getShoppingItemsUseCase() } returns flowOf(emptyList())
        viewModel = ShoppingViewModel(getShoppingItemsUseCase, addShoppingItemUseCase, updateShoppingItemUseCase, deleteShoppingItemUseCase)

        val newItemName = "Milk"
        val newItemCategory = Category.Milk

        viewModel.onItemNameChange(newItemName)
        viewModel.onCategoryChange(newItemCategory)
        viewModel.onSaveItem()

        testDispatcher.scheduler.runCurrent()

        val expectedItem = ShoppingItem(name = newItemName, category = newItemCategory)
        coVerify(exactly = 1) { addShoppingItemUseCase(expectedItem) }
        coVerify(exactly = 0) { updateShoppingItemUseCase(any()) }
    }

    @Test
    fun `onSaveItem should call updateShoppingItemUseCase for existing item`() = runTest {
        coEvery { getShoppingItemsUseCase() } returns flowOf(emptyList())
        viewModel = ShoppingViewModel(getShoppingItemsUseCase, addShoppingItemUseCase, updateShoppingItemUseCase, deleteShoppingItemUseCase)

        val existingItem = ShoppingItem(id = 1, name = "Bread", category = Category.Breads)
        viewModel.onEditItem(existingItem)

        val updatedName = "Whole Wheat Bread"
        viewModel.onItemNameChange(updatedName)
        viewModel.onSaveItem()

        testDispatcher.scheduler.runCurrent()

        val expectedItem = existingItem.copy(name = updatedName)
        coVerify(exactly = 1) { updateShoppingItemUseCase(expectedItem) }
        coVerify(exactly = 0) { addShoppingItemUseCase(any()) }
    }

    @Test
    fun `onDeleteItem should call deleteShoppingItemUseCase`() = runTest {
        coEvery { getShoppingItemsUseCase() } returns flowOf(emptyList())
        viewModel = ShoppingViewModel(getShoppingItemsUseCase, addShoppingItemUseCase, updateShoppingItemUseCase, deleteShoppingItemUseCase)

        val itemToDelete = ShoppingItem(id = 1, name = "Apples", category = Category.Fruits)
        viewModel.onDeleteItem(itemToDelete)

        testDispatcher.scheduler.runCurrent()

        coVerify(exactly = 1) { deleteShoppingItemUseCase(itemToDelete) }
    }

    @Test
    fun `state should be updated with items from use case`() = runTest {
        val items = listOf(
            ShoppingItem(id = 1, name = "Chicken", category = Category.Meats),
            ShoppingItem(id = 2, name = "Yogurt", category = Category.Milk)
        )
        coEvery { getShoppingItemsUseCase() } returns flowOf(items)

        viewModel = ShoppingViewModel(getShoppingItemsUseCase, addShoppingItemUseCase, updateShoppingItemUseCase, deleteShoppingItemUseCase)
        testDispatcher.scheduler.runCurrent()

        viewModel.uiState.test {
            val updatedState = awaitItem()
            assertEquals(listOf("Chicken", "Yogurt"), updatedState.filteredItems.map { it.name })

            cancelAndConsumeRemainingEvents()
        }
    }
}
