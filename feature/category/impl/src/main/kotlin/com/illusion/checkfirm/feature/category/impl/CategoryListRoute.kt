package com.illusion.checkfirm.feature.category.impl

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneCard
import com.illusion.checkfirm.core.designsystem.component.OneIcons
import com.illusion.checkfirm.core.designsystem.component.OneNavButton
import com.illusion.checkfirm.core.designsystem.component.OneScaffold
import com.illusion.checkfirm.core.domain.repository.BCRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.illusion.checkfirm.feature.category.R as FeatureR

@HiltViewModel
class CategoryListViewModel @Inject constructor(private val repository: BCRepository) : ViewModel() {
    val categories = repository.getAllCategory().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList(),
    )
    fun delete(name: String) = viewModelScope.launch { repository.deleteCategory(name) }
}

@Composable
fun CategoryListRoute(
    onNavigationIconClick: () -> Unit,
    onEditCategory: (String) -> Unit,
    onNewCategory: () -> Unit,
    viewModel: CategoryListViewModel = hiltViewModel(),
) {
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    OneScaffold(
        title = stringResource(R.string.category),
        navigationIcon = {
            OneNavButton(shape = androidx.compose.foundation.shape.CircleShape, modifier = Modifier.size(48.dp), onClick = onNavigationIconClick) {
                Icon(OneIcons.Back, contentDescription = stringResource(FeatureR.string.navigate_back))
            }
        },
        actions = {
            OneNavButton(shape = androidx.compose.foundation.shape.CircleShape, modifier = Modifier.size(48.dp), onClick = onNewCategory) {
                Icon(Icons.Rounded.Add, contentDescription = stringResource(FeatureR.string.category_add))
            }
        },
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(bottom = padding.calculateBottomPadding())) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(
                start = 12.dp, end = 12.dp,
                top = padding.calculateTopPadding() + 8.dp,
                bottom = 8.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(categories, key = { it.name }) { category ->
                OneCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            category.name,
                            modifier = Modifier.weight(1f)
                                .clickable { onEditCategory(category.name) }
                                .padding(horizontal = 14.dp, vertical = 13.dp),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        IconButton(onClick = { viewModel.delete(category.name) }) {
                            Icon(Icons.Rounded.Delete,
                                contentDescription = stringResource(FeatureR.string.category_delete, category.name))
                        }
                    }
                }
            }
        }
        com.illusion.checkfirm.core.designsystem.component.OneTab(
            titles = listOf(stringResource(R.string.bookmark), stringResource(R.string.category)),
            selectedTabIndex = 1, onTabSelected = { if (it == 0) onNavigationIconClick() },
        )
        }
    }
}
