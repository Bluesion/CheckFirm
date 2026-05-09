package com.illusion.checkfirm.feature.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneBottomSheetDialog
import com.illusion.checkfirm.core.designsystem.component.OneRadioButton
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

@Composable
fun CategoryDialog(
    selected: String,
    categories: List<String>,
    onDismiss: () -> Unit,
    onCategoryPick: (String) -> Unit,
) {
    val all = stringResource(R.string.category_all)
    val options = listOf(all) + categories

    OneBottomSheetDialog(
        title = stringResource(R.string.category),
        onDismiss = onDismiss,
    ) {
        Text(
            text = stringResource(R.string.main_category_dialog_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
        ) {
            options.forEach { name ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onCategoryPick(name)
                            onDismiss()
                        }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f),
                    )
                    OneRadioButton(
                        selected = name == selected,
                        onClick = {
                            onCategoryPick(name)
                            onDismiss()
                        },
                    )
                }
            }
        }
    }
}

@ComponentPreview
@Composable
private fun CategoryDialogPreview() {
    CheckFirmTheme {
        Surface {
            CategoryDialog(
                selected = "Galaxy S",
                categories = listOf("Galaxy S", "Galaxy Z", "Galaxy A"),
                onDismiss = {},
                onCategoryPick = {},
            )
        }
    }
}
