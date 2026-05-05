package com.illusion.checkfirm.feature.bookmark.impl.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.domain.model.Bookmark
import com.illusion.checkfirm.domain.model.Device

@Composable
internal fun BookmarkItem(
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

@ComponentPreview
@Composable
private fun BookmarkItemPreview() {
    CheckFirmTheme {
        BookmarkItem(
            bookmark = Bookmark(
                name = "Galaxy S24",
                device = Device(model = "SM-S928B", csc = "KOO"),
                category = "Galaxy S",
            ),
            onEditClick = {},
            onDeleteClick = {},
        )
    }
}
