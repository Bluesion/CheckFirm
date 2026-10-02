package com.illusion.checkfirm.feature.bookmark.impl

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.core.domain.model.Bookmark
import com.illusion.checkfirm.core.domain.model.Device

@Composable
internal fun BookmarkItem(
    bookmark: Bookmark,
    onClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = com.illusion.checkfirm.core.designsystem.component.OneCardShape,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
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
                    Text(" · ", style = MaterialTheme.typography.bodyMedium)
                    Text(bookmark.device.csc, style = MaterialTheme.typography.bodyMedium)
                }
            }
            IconButton(onClick = onEditClick) { Icon(androidx.compose.ui.res.painterResource(com.illusion.checkfirm.core.designsystem.R.drawable.oneui_ic_edit), androidx.compose.ui.res.stringResource(com.illusion.checkfirm.core.designsystem.R.string.edit_item)) }
            IconButton(onClick = onDeleteClick) { Icon(androidx.compose.ui.res.painterResource(com.illusion.checkfirm.core.designsystem.R.drawable.oneui_ic_delete), androidx.compose.ui.res.stringResource(com.illusion.checkfirm.core.designsystem.R.string.delete_item)) }
        }
    }
}

@ComponentPreview
@Composable
private fun BookmarkItemPreview() {
    CheckFirmTheme {
        Surface {
            BookmarkItem(
                bookmark = Bookmark(
                    name = "Galaxy S24",
                    device = Device(model = "SM-S928B", csc = "KOO"),
                    category = "Galaxy S",
                ),
                onClick = {},
                onEditClick = {},
                onDeleteClick = {},
            )
        }
    }
}
