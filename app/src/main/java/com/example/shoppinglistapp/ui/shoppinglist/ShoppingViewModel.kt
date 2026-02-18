package com.example.shoppinglistapp.ui.shoppinglist

import ShoppingUiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglistapp.data.model.Category
import com.example.shoppinglistapp.data.model.ShoppingItem
import com.example.shoppinglistapp.data.model.SortOption
import com.example.shoppinglistapp.domain.usecase.AddShoppingItemUseCase
import com.example.shoppinglistapp.domain.usecase.DeleteShoppingItemUseCase
import com.example.shoppinglistapp.domain.usecase.GetShoppingItemsUseCase
import com.example.shoppinglistapp.domain.usecase.UpdateShoppingItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val getShoppingItemsUseCase: GetShoppingItemsUseCase,
    private val addShoppingItemUseCase: AddShoppingItemUseCase,
    private val updateShoppingItemUseCase: UpdateShoppingItemUseCase,
    private val deleteShoppingItemUseCase: DeleteShoppingItemUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShoppingUiState())
    val uiState: StateFlow<ShoppingUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getShoppingItemsUseCase().collect { items ->
                _uiState.update { it.copy(items = items) }
                processItems()
            }
        }
    }

    fun onItemNameChange(name: String) {
        _uiState.update { it.copy(itemName = name) }
    }

    fun onCategoryChange(category: Category) {
        _uiState.update { it.copy(itemCategory = category) }
    }

    fun onUpdateItem(item: ShoppingItem) {
        viewModelScope.launch {
            updateShoppingItemUseCase(item)
        }
    }

    fun onDeleteItem(item: ShoppingItem) {
        viewModelScope.launch {
            deleteShoppingItemUseCase(item)
        }
    }

    fun onToggleCategoryFilter(category: Category) {
        _uiState.update { currentState ->
            val newSelectedCategories = if (currentState.selectedCategories.contains(category)) {
                currentState.selectedCategories - category
            } else {
                currentState.selectedCategories + category
            }
            currentState.copy(selectedCategories = newSelectedCategories.toSet())
        }
        processItems()
    }

    fun onSortOptionChange(sortOption: SortOption) {
        _uiState.update { it.copy(sortOption = sortOption) }
        processItems()
    }

    fun onShowAddDialog(show: Boolean) {
        if (!show) {
            _uiState.update { it.copy(showAddDialog = false, itemToEdit = null, itemName = "", itemCategory = Category.Milk) }
        } else {
            _uiState.update { it.copy(showAddDialog = true) }
        }
    }

    fun onEditItem(item: ShoppingItem?) {
        _uiState.update {
            it.copy(
                itemToEdit = item,
                itemName = item?.name ?: "",
                itemCategory = item?.category ?: Category.Milk,
                showAddDialog = true
            )
        }
    }

    fun onSaveItem() {
        val state = _uiState.value
        if (state.itemName.isBlank()) return

        val item = state.itemToEdit?.copy(
            name = state.itemName,
            category = state.itemCategory
        ) ?: ShoppingItem(
            name = state.itemName,
            category = state.itemCategory
        )

        viewModelScope.launch {
            if (state.itemToEdit == null) {
                addShoppingItemUseCase(item)
            } else {
                updateShoppingItemUseCase(item)
            }
        }
        onShowAddDialog(false)
    }


    private fun processItems() {
        val state = _uiState.value
        val filteredList = if (state.selectedCategories.isEmpty()) {
            state.items
        } else {
            state.items.filter { it.category in state.selectedCategories }
        }

        val sortedList = when (state.sortOption) {
            SortOption.ALPHABETICAL -> filteredList.sortedBy { it.name }
            SortOption.BY_CATEGORY -> filteredList.sortedBy { it.category.name }
            SortOption.BY_STATUS -> filteredList.sortedBy { it.isCompleted }
        }

        _uiState.update { it.copy(filteredItems = sortedList) }
    }
}