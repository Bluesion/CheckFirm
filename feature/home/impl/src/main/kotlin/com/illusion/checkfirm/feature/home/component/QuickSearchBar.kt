package com.illusion.checkfirm.feature.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneIcons
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.core.domain.model.Bookmark
import com.illusion.checkfirm.core.domain.model.Device

@Composable
internal fun QuickSearchBar(
    bookmarks: List<Bookmark>,
    onCategoryIconClick: () -> Unit,
    onBookmarkClick: (Bookmark) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable(onClick = onCategoryIconClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = OneIcons.Category,
                contentDescription = stringResource(R.string.category),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .size(24.dp)
                    .padding(4.dp),
            )
        }
        Spacer(Modifier.width(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(
                count = bookmarks.size,
                key = { bookmarks[it].device.model + bookmarks[it].device.csc },
            ) { idx ->
                val bookmark = bookmarks[idx]
                AssistChip(
                    onClick = { onBookmarkClick(bookmark) },
                    label = { Text(bookmark.name) },
                    shape = CircleShape,
                    colors = AssistChipDefaults.assistChipColors(
                        labelColor = MaterialTheme.colorScheme.onSurface,
                    ),
                )
            }
        }
    }
}

@ComponentPreview
@Composable
fun QuickSearchBarPreview() {
    CheckFirmTheme {
        Surface {
            QuickSearchBar(
                bookmarks = listOf(
                    Bookmark("S24", Device("SM-S928B", "KOO"), "Galaxy S"),
                    Bookmark("Z Fold5", Device("SM-F946B", "KOO"), "Galaxy Z"),
                ),
                onCategoryIconClick = {},
                onBookmarkClick = {},
            )
        }
    }
}
