package com.illusion.checkfirm.feature.settings.bookmark

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R

@Composable
fun BookmarkOrderDialog(
    selectedOrder: String,
    isAscending: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (order: String, ascending: Boolean) -> Unit,
) {
    var order by remember(selectedOrder) { mutableStateOf(selectedOrder) }
    var ascending by remember(isAscending) { mutableStateOf(isAscending) }

    val options = listOf(
        "time" to stringResource(R.string.settings_bookmark_order_by_created_time),
        "device" to stringResource(R.string.settings_bookmark_order_by_device),
        "name" to stringResource(R.string.settings_bookmark_order_by_name),
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.settings_bookmark_order)) },
        text = {
            Column(Modifier.fillMaxWidth()) {
                options.forEach { (key, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { order = key }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        RadioButton(selected = order == key, onClick = { order = key })
                        Text(label)
                    }
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { ascending = !ascending }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Checkbox(checked = !ascending, onCheckedChange = { ascending = !it })
                    Text(text = stringResource(R.string.settings_bookmark_order_by_desc))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(order, ascending) }) {
                Text(text = stringResource(R.string.bookmark_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(text = stringResource(R.string.close)) }
        }
    )
}
