package com.illusion.checkfirm.feature.bookmark.impl.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.domain.model.Bookmark

@Composable
fun BookmarkContent(
    uiState: BookmarkListUiState,
    onExpandedChange: (Boolean) -> Unit,
    onCategoryChange: (String) -> Unit,
    onItemClick: (Bookmark) -> Unit,
    onEditClick: (Bookmark) -> Unit,
    onDeleteClick: (Bookmark) -> Unit,
) {
    val allLabel = stringResource(R.string.category_all)
    val categories = listOf(allLabel) + uiState.categories.map { it.name }
    val displayedSelection = uiState.selectedCategory.ifBlank { allLabel }

    Column(modifier = Modifier.fillMaxSize()) {
        ExposedDropdownMenuBox(
            expanded = uiState.expanded,
            onExpandedChange = onExpandedChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            OutlinedTextField(
                value = displayedSelection,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = uiState.expanded) },
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = uiState.expanded,
                onDismissRequest = { onExpandedChange(false) }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category) },
                        onClick = {
                            // Convert "All" label back to empty-string sentinel
                            onCategoryChange(if (category == allLabel) "" else category)
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }

        if (uiState.bookmarks.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.search_no_bookmark),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = uiState.bookmarks,
                    key = { it.device.model + it.device.csc }
                ) { item ->
                    BookmarkItem(
                        bookmark = item,
                        onClick = { onItemClick(item) },
                        onEditClick = { onEditClick(item) },
                        onDeleteClick = { onDeleteClick(item) }
                    )
                }
            }
        }
    }
}
