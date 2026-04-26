package com.illusion.checkfirm.feature.bookmark.impl.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

@Composable
fun CategoryDialog(
    initial: String?,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var name by remember(initial) { mutableStateOf(initial.orEmpty()) }
    val isAll = name == stringResource(R.string.category_all)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.category))
        },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(text = stringResource(R.string.category_edit_name)) },
                singleLine = true,
                isError = isAll,
                supportingText = {
                    if (isAll) Text(text = stringResource(R.string.category_edit_name_error_all))
                },
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(name.trim()) },
                enabled = name.isNotBlank() && !isAll
            ) {
                Text(text = stringResource(R.string.bookmark_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(text = stringResource(R.string.close)) }
        }
    )
}

@ComponentPreview
@Composable
private fun CategoryDialogPreview() {
    CheckFirmTheme {
        CategoryDialog(
            initial = null,
            onDismiss = {},
            onConfirm = {},
        )
    }
}
