package com.illusion.checkfirm.feature.bookmark.impl.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneIcons
import com.illusion.checkfirm.core.designsystem.component.OneScaffold
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.domain.model.Bookmark
import com.illusion.checkfirm.domain.model.Category

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkListScreen(
    uiState: BookmarkListUiState = BookmarkListUiState(),
    onExpandedChange: (Boolean) -> Unit = {},
    onCategoryChange: (String) -> Unit = {},
    onNavigationIconClick: () -> Unit,
    onAddBookmark: (Bookmark) -> Unit = {},
    onEditBookmark: (Bookmark) -> Unit = {},
    onDeleteBookmark: (String) -> Unit = {},
    onAddCategory: (Category) -> Unit = {},
    onEditCategory: (Category) -> Unit = {},
    onDeleteCategory: (String) -> Unit = {},
) {
    var selectedTab by remember { mutableStateOf(0) }
    var editingBookmark by remember { mutableStateOf<Bookmark?>(null) }
    var showNewBookmark by remember { mutableStateOf(false) }
    var editingCategory by remember { mutableStateOf<Category?>(null) }
    var showNewCategory by remember { mutableStateOf(false) }

    OneScaffold(
        title = stringResource(R.string.bookmark),
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    imageVector = OneIcons.IcBack,
                    contentDescription = null,
                    tint = CheckFirmTheme.colors.toolbarIconTint,
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (selectedTab == 0) showNewBookmark = true else showNewCategory = true
                },
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text(stringResource(R.string.bookmark)) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text(stringResource(R.string.category)) }
                )
            }

            when (selectedTab) {
                0 -> BookmarkTab(
                    uiState = uiState,
                    onExpandedChange = onExpandedChange,
                    onCategoryChange = onCategoryChange,
                    onEditClick = { editingBookmark = it },
                    onDeleteClick = { onDeleteBookmark(it.device.model) }
                )

                1 -> CategoryTab(
                    categories = uiState.categories,
                    onEditClick = { editingCategory = it },
                    onDeleteClick = { onDeleteCategory(it.name) }
                )
            }
        }
    }

    val categoryNames =
        listOf(stringResource(R.string.category_all)) + uiState.categories.map { it.name }

    if (showNewBookmark) {
        BookmarkDialog(
            categories = categoryNames.drop(1),
            onDismiss = { showNewBookmark = false },
            onConfirm = {
                onAddBookmark(it)
                showNewBookmark = false
            }
        )
    }

    editingBookmark?.let { bm ->
        BookmarkDialog(
            initial = bm,
            categories = categoryNames.drop(1),
            onDismiss = { editingBookmark = null },
            onConfirm = {
                onEditBookmark(it)
                editingBookmark = null
            }
        )
    }

    if (showNewCategory) {
        CategoryDialog(
            initial = null,
            onDismiss = { showNewCategory = false },
            onConfirm = {
                onAddCategory(Category(it))
                showNewCategory = false
            }
        )
    }

    editingCategory?.let { cat ->
        CategoryDialog(
            initial = cat.name,
            onDismiss = { editingCategory = null },
            onConfirm = {
                onEditCategory(Category(it))
                editingCategory = null
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookmarkTab(
    uiState: BookmarkListUiState,
    onExpandedChange: (Boolean) -> Unit,
    onCategoryChange: (String) -> Unit,
    onEditClick: (Bookmark) -> Unit,
    onDeleteClick: (Bookmark) -> Unit,
) {
    val categories =
        listOf(stringResource(R.string.category_all)) + uiState.categories.map { it.name }

    Column(modifier = Modifier.fillMaxSize()) {
        ExposedDropdownMenuBox(
            expanded = uiState.expanded,
            onExpandedChange = onExpandedChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            OutlinedTextField(
                value = uiState.selectedCategory,
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
                            onCategoryChange(category)
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }

        if (uiState.bookmarks.isEmpty()) {
            EmptyMessage(text = stringResource(R.string.search_no_bookmark))
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
                        onEditClick = { onEditClick(item) },
                        onDeleteClick = { onDeleteClick(item) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryTab(
    categories: List<Category>,
    onEditClick: (Category) -> Unit,
    onDeleteClick: (Category) -> Unit,
) {
    if (categories.isEmpty()) {
        EmptyMessage(text = "No categories")
        return
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = categories, key = { it.name }) { category ->
            CategoryItem(
                category = category,
                onEditClick = { onEditClick(category) },
                onDeleteClick = { onDeleteClick(category) }
            )
        }
    }
}

@Composable
private fun EmptyMessage(text: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun BookmarkItem(
    bookmark: Bookmark,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = bookmark.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(bookmark.device.model, style = MaterialTheme.typography.bodyMedium)
                    Text(" / ", style = MaterialTheme.typography.bodyMedium)
                    Text(bookmark.device.csc, style = MaterialTheme.typography.bodyMedium)
                    if (bookmark.category.isNotBlank()) {
                        Text(
                            "  ·  ${bookmark.category}",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
            IconButton(onClick = onEditClick) { Icon(Icons.Rounded.Edit, "Edit") }
            IconButton(onClick = onDeleteClick) { Icon(Icons.Rounded.Delete, "Delete") }
        }
    }
}

@Composable
private fun CategoryItem(
    category: Category,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = category.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onEditClick) { Icon(Icons.Rounded.Edit, "Edit") }
            IconButton(onClick = onDeleteClick) { Icon(Icons.Rounded.Delete, "Delete") }
        }
    }
}
