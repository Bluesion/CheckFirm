package com.illusion.checkfirm.feature.home.component

import androidx.compose.foundation.background
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
import androidx.compose.material3.FilterChip
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

@Composable
internal fun QuickSearchBar(
    selected: String,
    categories: List<String>,
    onChipClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = OneIcons.IcCategory,
                contentDescription = stringResource(R.string.category),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .size(24.dp)
                    .padding(4.dp),
            )
        }
        Spacer(Modifier.width(8.dp))
        val all = listOf(stringResource(R.string.category_all)) + categories
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(count = all.size, key = { all[it] }) { idx ->
                val chip = all[idx]
                FilterChip(
                    selected = chip == selected,
                    onClick = { onChipClick(chip) },
                    label = { Text(chip) },
                    shape = CircleShape,
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
                selected = "A",
                categories = listOf("A", "B", "C"),
                onChipClick = {},
            )
        }
    }
}
