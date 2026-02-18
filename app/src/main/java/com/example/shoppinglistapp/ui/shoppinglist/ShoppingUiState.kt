import com.example.shoppinglistapp.data.model.Category
import com.example.shoppinglistapp.data.model.ShoppingItem
import com.example.shoppinglistapp.data.model.SortOption

data class ShoppingUiState(
    val items: List<ShoppingItem> = emptyList(),
    val filteredItems: List<ShoppingItem> = emptyList(),
    val categories: List<Category> = Category.values().toList(),
    val selectedCategories: Set<Category> = emptySet(),
    val itemToEdit: ShoppingItem? = null,
    val itemName: String = "",
    val itemCategory: Category = Category.Milk,
    val sortOption: SortOption = SortOption.ALPHABETICAL
)