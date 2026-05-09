package com.illusion.checkfirm.feature.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.component.OneCard
import com.illusion.checkfirm.core.designsystem.component.OneCheckboxCard
import com.illusion.checkfirm.core.designsystem.component.OneIcons
import com.illusion.checkfirm.core.designsystem.component.OneLoadingIndicator
import com.illusion.checkfirm.core.designsystem.component.OneNavButton
import com.illusion.checkfirm.core.designsystem.component.OneScaffold
import com.illusion.checkfirm.core.designsystem.preview.ScreenPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.core.designsystem.R as DesignSystemR

@Composable
fun ReportScreen(
    uiState: ReportUiState,
    onBugTypeToggle: (BugType) -> Unit,
    onLogsChange: (String) -> Unit,
    onSubmitClick: () -> Unit,
    onNavigationIconClick: () -> Unit,
) {
    OneScaffold(
        title = stringResource(DesignSystemR.string.report),
        navigationIcon = {
            OneNavButton(
                icon = OneIcons.IcBack,
                onClick = onNavigationIconClick,
            )
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
            OneCard(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = stringResource(R.string.report_description),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            OneCheckboxCard(
                text = stringResource(R.string.report_type_1),
                isChecked = BugType.FIRMWARE_INFO_ERROR in uiState.bugTypes,
                onCheckedChange = { onBugTypeToggle(BugType.FIRMWARE_INFO_ERROR) },
                modifier = Modifier.fillMaxWidth(),
            )

            OneCheckboxCard(
                text = stringResource(R.string.report_type_2),
                isChecked = BugType.INAPPROPRIATE_USER_NAME in uiState.bugTypes,
                onCheckedChange = { onBugTypeToggle(BugType.INAPPROPRIATE_USER_NAME) },
                modifier = Modifier.fillMaxWidth()
            )

            OneCheckboxCard(
                text = stringResource(R.string.report_type_3),
                isChecked = BugType.SMART_SEARCH_INFO_ERROR in uiState.bugTypes,
                onCheckedChange = { onBugTypeToggle(BugType.SMART_SEARCH_INFO_ERROR) },
                modifier = Modifier.fillMaxWidth()
            )

            OneCheckboxCard(
                text = stringResource(R.string.report_type_4),
                isChecked = BugType.OTHER_ERROR in uiState.bugTypes,
                onCheckedChange = { onBugTypeToggle(BugType.OTHER_ERROR) },
                modifier = Modifier.fillMaxWidth()
            )

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
                    OneLoadingIndicator(
                        modifier = Modifier.size(96.dp),
                    )
                } else {
                    Text(text = stringResource(R.string.report_submit))
                }
            }
        }
    }
}

@ScreenPreview
@Composable
private fun ReportScreenPreview() {
    CheckFirmTheme {
        Surface {
            ReportScreen(
                uiState = ReportUiState(isSubmitting = true),
                onBugTypeToggle = {},
                onLogsChange = {},
                onSubmitClick = {},
                onNavigationIconClick = {},
            )
        }
    }
}
