package com.example.shoppinglistapp.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.shoppinglistapp.data.model.ShoppingItem

@Composable
fun ShoppingItemRow(
    item: ShoppingItem,
    onItemCheckedChanged: (ShoppingItem, Boolean) -> Unit,
    onEditClick: (ShoppingItem) -> Unit,
    onDeleteClick: (ShoppingItem) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.isCompleted,
                onCheckedChange = { isChecked ->
                    onItemCheckedChanged(item, isChecked)
                }
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = item.name,
                textDecoration = if (item.isCompleted) TextDecoration.LineThrough else TextDecoration.None
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { onEditClick(item) }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Item")
            }
            IconButton(onClick = { onDeleteClick(item) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Item")
            }
        }
    }
}