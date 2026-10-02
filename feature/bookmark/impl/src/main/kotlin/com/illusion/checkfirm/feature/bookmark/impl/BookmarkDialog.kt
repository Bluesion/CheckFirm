package com.illusion.checkfirm.feature.bookmark.impl

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.TextButton
import com.illusion.checkfirm.core.designsystem.component.OneSpinner
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.MaterialTheme
import android.widget.Toast
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
    val context = LocalContext.current
    val allLabel = stringResource(R.string.category_all)
    var name by remember { mutableStateOf(initial?.name.orEmpty()) }
    var model by remember { mutableStateOf(initial?.device?.model ?: "SM-") }
    var csc by remember { mutableStateOf(initial?.device?.csc.orEmpty()) }
    var category by remember {
        mutableStateOf(
            initial?.category.orEmpty()
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
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(model, { model = it.uppercase(java.util.Locale.US) }, label = { Text(stringResource(R.string.model)) }, singleLine = true,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters), modifier = Modifier.weight(1f))
                OutlinedTextField(csc, { if (it.length <= 3) csc = it.uppercase(java.util.Locale.US) }, label = { Text(stringResource(R.string.csc)) }, singleLine = true,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters), modifier = Modifier.weight(1f))
            }
            if (categories.any { it != allLabel }) {
                Text(stringResource(R.string.category), Modifier.padding(top = 8.dp))
                OneSpinner(categories, category.ifBlank { allLabel }, { category = if (it == allLabel) "" else it }, Modifier.fillMaxWidth())
            }
        }

        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth()) {
            TextButton(onDismiss, Modifier.weight(1f)) { Text(stringResource(android.R.string.cancel), color = MaterialTheme.colorScheme.onSurface) }
            TextButton(onClick = {
                val device = Device(model.trim().uppercase(java.util.Locale.US), csc.trim().uppercase(java.util.Locale.US))
                val error = if (name.isBlank()) FeatureR.string.bookmark_name_error else if (!device.isValidDevice()) R.string.check_device else null
                if (error != null) Toast.makeText(context, context.getString(error), Toast.LENGTH_SHORT).show()
                else onConfirm(Bookmark(name.trim(), device, category, initial?.id, initial?.position ?: 0))
            }, modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.bookmark_save), color = MaterialTheme.colorScheme.onSurface)
            }
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
