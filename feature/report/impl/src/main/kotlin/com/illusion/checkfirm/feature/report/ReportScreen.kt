package com.illusion.checkfirm.feature.report

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneIcons
import com.illusion.checkfirm.core.designsystem.component.OneNavButton
import com.illusion.checkfirm.core.designsystem.component.OneScaffold
import com.illusion.checkfirm.core.designsystem.preview.ScreenPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

private val BUG_TYPE_KEYS = listOf("type_1", "type_2", "type_3", "type_4")

@Composable
fun ReportScreen(
    uiState: ReportUiState,
    onBugTypeToggle: (String) -> Unit,
    onLogsChange: (String) -> Unit,
    onSubmitClick: () -> Unit,
    onNavigationIconClick: () -> Unit,
) {
    val labels = mapOf(
        "type_1" to stringResource(R.string.report_type_1),
        "type_2" to stringResource(R.string.report_type_2),
        "type_3" to stringResource(R.string.report_type_3),
        "type_4" to stringResource(R.string.report_type_4),
    )

    OneScaffold(
        title = stringResource(R.string.report),
        navigationIcon = {
            OneNavButton(icon = OneIcons.IcBack, onClick = onNavigationIconClick)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(top = 12.dp, bottom = innerPadding.calculateBottomPadding() + 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            ) {
                Text(
                    text = stringResource(R.string.report_description),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                )
            }

            BUG_TYPE_KEYS.forEach { key ->
                ReportTypeRow(
                    title = labels.getValue(key),
                    checked = key in uiState.bugTypes,
                    onCheckedChange = { onBugTypeToggle(key) },
                )
            }

            OutlinedTextField(
                value = uiState.logs,
                onValueChange = onLogsChange,
                placeholder = { Text(text = stringResource(R.string.report_detail)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                minLines = 4,
            )

            Button(
                onClick = onSubmitClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                enabled = uiState.bugTypes.isNotEmpty() && !uiState.isSubmitting,
            ) {
                if (uiState.isSubmitting) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text(text = stringResource(R.string.report_submit))
                }
            }
        }
    }
}

@Composable
private fun ReportTypeRow(
    title: String,
    checked: Boolean,
    onCheckedChange: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange() }
            .padding(vertical = 4.dp),
    ) {
        Checkbox(checked = checked, onCheckedChange = { onCheckedChange() })
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}

@ScreenPreview
@Composable
private fun ReportScreenPreview() {
    CheckFirmTheme {
        Surface {
            ReportScreen(
                uiState = ReportUiState(),
                onBugTypeToggle = {},
                onLogsChange = {},
                onSubmitClick = {},
                onNavigationIconClick = {},
            )
        }
    }
}
