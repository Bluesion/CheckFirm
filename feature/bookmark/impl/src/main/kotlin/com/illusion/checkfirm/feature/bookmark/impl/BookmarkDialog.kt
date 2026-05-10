package com.illusion.checkfirm.feature.bookmark.impl

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneBottomSheetDialog
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.core.domain.model.Bookmark
import com.illusion.checkfirm.core.domain.model.Device
import com.illusion.checkfirm.feature.bookmark.R as FeatureR

@Composable
fun BookmarkDialog(
    initial: Bookmark? = null,
    categories: List<String>,
    onDismiss: () -> Unit,
    onConfirm: (Bookmark) -> Unit,
) {
    var name by remember { mutableStateOf(initial?.name.orEmpty()) }
    var model by remember { mutableStateOf(initial?.device?.model.orEmpty()) }
    var csc by remember { mutableStateOf(initial?.device?.csc.orEmpty()) }
    var category by remember {
        mutableStateOf(
            initial?.category ?: categories.firstOrNull().orEmpty()
        )
    }
    var categoryExpanded by remember { mutableStateOf(false) }

    OneBottomSheetDialog(
        title = stringResource(
            if (initial == null) FeatureR.string.bookmark_new else FeatureR.string.bookmark_edit,
        ),
        onDismiss = onDismiss,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(FeatureR.string.bookmark_name)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = model,
                onValueChange = { model = it.uppercase() },
                label = { Text(stringResource(R.string.model)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = csc,
                onValueChange = { if (it.length <= 3) csc = it.uppercase() },
                label = { Text(stringResource(R.string.csc)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
                modifier = Modifier.fillMaxWidth(),
            )
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = category,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.category)) },
                    trailingIcon = {
                        IconButton(onClick = { categoryExpanded = true }) {
                            Icon(Icons.Rounded.ArrowDropDown, contentDescription = null)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { categoryExpanded = true },
                )
                DropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false },
                ) {
                    categories.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = {
                                category = item
                                categoryExpanded = false
                            },
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                onConfirm(Bookmark(name.trim(), Device(model.trim(), csc.trim()), category))
            },
            enabled = name.isNotBlank() && model.isNotBlank() && csc.length == 3 && category.isNotBlank(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
        ) {
            Text(stringResource(R.string.bookmark_save))
        }
    }
}

@ComponentPreview
@Composable
private fun BookmarkDialogPreview() {
    CheckFirmTheme {
        Surface {
            BookmarkDialog(
                categories = listOf("Galaxy S", "Galaxy Z"),
                onDismiss = {},
                onConfirm = {},
            )
        }
    }
}
