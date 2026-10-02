package com.illusion.checkfirm.feature.settings.bookmark

import androidx.compose.foundation.clickable
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.ui.semantics.Role
import androidx.compose.material3.TextButton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.component.OneBottomSheetDialog
import com.illusion.checkfirm.core.designsystem.component.OneRadioButton
import com.illusion.checkfirm.core.designsystem.component.OneSwitch
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.feature.settings.R

/**
 * Apply-immediately BottomSheet (matches the legacy XML behavior).
 * The legacy version dismissed only on OK but also pushed the change on each
 * radio tap; we mirror that by invoking [onConfirm] eagerly on every change.
 */
@Composable
fun BookmarkOrderDialog(
    selectedOrder: String,
    isAscending: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (order: String, ascending: Boolean) -> Unit,
) {
    val options = listOf(
        "time" to stringResource(R.string.settings_bookmark_order_by_created_time),
        "device" to stringResource(R.string.settings_bookmark_order_by_device),
        "name" to stringResource(R.string.settings_bookmark_order_by_name),
    )

    OneBottomSheetDialog(
        title = stringResource(R.string.settings_bookmark_order),
        onDismiss = onDismiss,
    ) {
        Column(Modifier.fillMaxWidth()) {
            options.forEach { (key, label) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(selected = selectedOrder == key, role = Role.RadioButton) { onConfirm(key, isAscending) }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f),
                    )
                    OneRadioButton(
                        selected = selectedOrder == key,
                        onClick = null,
                    )
                }
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .toggleable(value = !isAscending, role = Role.Switch) { onConfirm(selectedOrder, !it) }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.settings_bookmark_order_by_desc),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f),
                )
                OneSwitch(
                    checked = !isAscending,
                    onCheckedChange = null,
                )
            }
        }
        TextButton(onDismiss, Modifier.fillMaxWidth()) { Text(stringResource(android.R.string.ok), color = MaterialTheme.colorScheme.onSurface) }
    }
}

@ComponentPreview
@Composable
private fun BookmarkOrderDialogPreview() {
    CheckFirmTheme {
        Surface {
            BookmarkOrderDialog(
                selectedOrder = "time",
                isAscending = true,
                onDismiss = {},
                onConfirm = { _, _ -> },
            )
        }
    }
}
