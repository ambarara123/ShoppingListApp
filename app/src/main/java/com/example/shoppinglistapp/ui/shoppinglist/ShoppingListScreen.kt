package com.example.shoppinglistapp.ui.shoppinglist

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shoppinglistapp.data.model.Category
import com.example.shoppinglistapp.data.model.SortOption
import com.example.shoppinglistapp.ui.composables.AddItemForm
import com.example.shoppinglistapp.ui.composables.ShoppingItemRow

@Composable
fun ShoppingListScreen(
    viewModel: ShoppingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    val lazyListState = rememberLazyListState()
    var showAddDialog by remember { mutableStateOf(false) }

    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .statusBarsPadding()
            .imePadding()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
            .pointerInput(Unit) {
                detectVerticalDragGestures { _, _ ->
                    focusManager.clearFocus()
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            GroceryHeader(
                selectedCategories = uiState.selectedCategories,
                sortOption = uiState.sortOption,
                onToggleCategoryFilter = viewModel::onToggleCategoryFilter,
                onSortOptionChange = viewModel::onSortOptionChange
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            AddItemForm(
                itemName = uiState.itemName,
                selectedCategory = uiState.itemCategory,
                onItemNameChange = viewModel::onItemNameChange,
                onCategoryChange = viewModel::onCategoryChange,
                onSaveItem = {
                    viewModel.onSaveItem()
                    showAddDialog = false
                },
                showForm = showAddDialog,
                onToggleForm = { showAddDialog = !showAddDialog },
                isEditing = uiState.itemToEdit != null
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        if (uiState.filteredItems.isEmpty()) {
            item {
                EmptyState()
            }
        } else {
            items(uiState.filteredItems) { item ->
                ShoppingItemRow(
                    item = item,
                    onItemCheckedChanged = { _, isChecked ->
                        viewModel.onUpdateItem(item.copy(isCompleted = isChecked))
                    },
                    onEditClick = {
                        viewModel.onEditItem(item)
                        showAddDialog = true
                    },
                    onDeleteClick = {
                        viewModel.onDeleteItem(item)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun GroceryHeader(
    selectedCategories: Set<Category>,
    sortOption: SortOption,
    onToggleCategoryFilter: (Category) -> Unit,
    onSortOptionChange: (SortOption) -> Unit
) {
    var showFilterMenu by remember { mutableStateOf(false) }
    var showSortMenu by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(top = 32.dp)
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(androidx.compose.foundation.shape.CircleShape)
                .background(com.example.shoppinglistapp.ui.theme.Gradient.Purple),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Shopping Cart",
                tint = Color.White,
                modifier = Modifier.size(50.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Grocery List",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Add items to your shopping list",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sort by: ${sortOption.displayName}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Box {
                    IconButton(onClick = { showSortMenu = true }) {
                        Icon(Icons.AutoMirrored.Filled.Sort, contentDescription = "Sort")
                    }
                    SortMenu(
                        expanded = showSortMenu,
                        onDismissRequest = { showSortMenu = false },
                        onSortOptionSelected = { option ->
                            onSortOptionChange(option)
                            showSortMenu = false
                        }
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filter by:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Box {
                    IconButton(onClick = { showFilterMenu = true }) {
                        Icon(Icons.Filled.FilterList, contentDescription = "Filter")
                    }
                    FilterMenu(
                        expanded = showFilterMenu,
                        onDismissRequest = { showFilterMenu = false },
                        categories = Category.values().toList(),
                        selectedCategories = selectedCategories,
                        onToggleCategoryFilter = { category ->
                            onToggleCategoryFilter(category)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SortMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onSortOptionSelected: (SortOption) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        SortOption.values().forEach { option ->
            DropdownMenuItem(
                text = { Text(option.displayName) },
                onClick = { onSortOptionSelected(option) }
            )
        }
    }
}

@Composable
fun FilterMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    categories: List<Category>,
    selectedCategories: Set<Category>,
    onToggleCategoryFilter: (Category) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            text = { Text("All") },
            onClick = {
                selectedCategories.forEach { category ->
                    onToggleCategoryFilter(category)
                }
                onDismissRequest()
            }
        )
        categories.forEach { category ->
            DropdownMenuItem(
                text = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(category.name)
                        Checkbox(
                            checked = selectedCategories.contains(category),
                            onCheckedChange = { onToggleCategoryFilter(category) }
                        )
                    }
                },
                onClick = { onToggleCategoryFilter(category) }
            )
        }
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "Empty grocery list",
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Your grocery list is empty",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Add items above to get started",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}