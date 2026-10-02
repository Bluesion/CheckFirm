package com.illusion.checkfirm.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

@Composable
fun <T> OneSpinner(
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    itemToString: (T) -> String = { it.toString() }
) {
    var expanded by remember { mutableStateOf(false) }
    val textColor = MaterialTheme.colorScheme.onSurface

    Box(modifier = modifier) {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier.fillMaxWidth().clickable(role = androidx.compose.ui.semantics.Role.Button) { expanded = true }.padding(8.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        ) {
            Text(itemToString(selectedItem), Modifier.weight(1f), color = textColor, style = MaterialTheme.typography.bodyLarge)
            androidx.compose.material3.Icon(
                androidx.compose.material.icons.Icons.Default.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(),
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = itemToString(item),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun OneSpinnerPreview() {
    CheckFirmTheme {
        Surface {
            OneSpinner(
                items = listOf("Option A", "Option B", "Option C"),
                selectedItem = "Option A",
                onItemSelected = {},
            )
        }
    }
}

