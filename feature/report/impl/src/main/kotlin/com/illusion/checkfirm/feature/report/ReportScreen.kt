package com.illusion.checkfirm.feature.report

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    onUserMessageUpdate: (String) -> Unit,
    onSubmitClick: () -> Unit,
    onNavigationIconClick: () -> Unit,
) {
    OneScaffold(
        title = stringResource(DesignSystemR.string.report),
        navigationIcon = {
            OneNavButton(
                onClick = onNavigationIconClick,
                shape = CircleShape,
                modifier = Modifier.size(48.dp),
            ) {
                Icon(
                    imageVector = OneIcons.Back,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp),
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(bottom = innerPadding.calculateBottomPadding() + 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = innerPadding.calculateTopPadding() + 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
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

            Spacer(modifier = Modifier.height(24.dp))

            OneCheckboxCard(
                text = stringResource(R.string.report_type_1),
                isChecked = BugType.FIRMWARE_INFO_ERROR in uiState.bugTypes,
                onCheckedChange = { onBugTypeToggle(BugType.FIRMWARE_INFO_ERROR) },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(8.dp))

            OneCheckboxCard(
                text = stringResource(R.string.report_type_2),
                isChecked = BugType.INAPPROPRIATE_USER_NAME in uiState.bugTypes,
                onCheckedChange = { onBugTypeToggle(BugType.INAPPROPRIATE_USER_NAME) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OneCheckboxCard(
                text = stringResource(R.string.report_type_3),
                isChecked = BugType.SMART_SEARCH_INFO_ERROR in uiState.bugTypes,
                onCheckedChange = { onBugTypeToggle(BugType.SMART_SEARCH_INFO_ERROR) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OneCheckboxCard(
                text = stringResource(R.string.report_type_4),
                isChecked = BugType.OTHER_ERROR in uiState.bugTypes,
                onCheckedChange = { onBugTypeToggle(BugType.OTHER_ERROR) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = uiState.userMessage,
                onValueChange = onUserMessageUpdate,
                placeholder = { Text(text = stringResource(R.string.report_detail)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onSubmitClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 56.dp)
                    .padding(top = 16.dp),
                enabled = uiState.bugTypes.isNotEmpty() && !uiState.isSubmitting,
            ) {
                if (uiState.isSubmitting) {
                    OneLoadingIndicator(
                        modifier = Modifier.size(24.dp),
                        tint = Color.White,
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
                onUserMessageUpdate = {},
                onSubmitClick = {},
                onNavigationIconClick = {},
            )
        }
    }
}
