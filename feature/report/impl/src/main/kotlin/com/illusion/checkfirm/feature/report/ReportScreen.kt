package com.illusion.checkfirm.feature.report

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.selection.toggleable
import androidx.compose.ui.semantics.Role
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.Alignment
import androidx.compose.ui.semantics.clearAndSetSemantics
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
    var messageFocused by remember { mutableStateOf(false) }
    OneScaffold(
        title = stringResource(DesignSystemR.string.report),
        actions = {
            androidx.compose.material3.IconButton(onClick = onSubmitClick, enabled = !uiState.isSubmitting) {
                if (uiState.isSubmitting) OneLoadingIndicator(Modifier.size(24.dp))
                else Icon(androidx.compose.ui.res.painterResource(DesignSystemR.drawable.ic_send), stringResource(R.string.report_submit), tint = MaterialTheme.colorScheme.onSurface)
            }
        },
        navigationIcon = {
            OneNavButton(
                onClick = onNavigationIconClick,
                shape = CircleShape,
                modifier = Modifier.size(48.dp),
            ) {
                Icon(
                    imageVector = OneIcons.Back,
                    contentDescription = stringResource(com.illusion.checkfirm.core.designsystem.R.string.navigate_back),
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

            Spacer(Modifier.height(16.dp))
            listOf(
                BugType.FIRMWARE_INFO_ERROR to R.string.report_type_1,
                BugType.INAPPROPRIATE_USER_NAME to R.string.report_type_2,
                BugType.SMART_SEARCH_INFO_ERROR to R.string.report_type_3,
                BugType.OTHER_ERROR to R.string.report_type_4,
            ).forEach { (type, label) ->
                Row(
                    Modifier.fillMaxWidth().toggleable(value = type in uiState.bugTypes, role = Role.Checkbox) { onBugTypeToggle(type) },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    com.illusion.checkfirm.core.designsystem.component.OneCheckbox(type in uiState.bugTypes, { onBugTypeToggle(type) }, Modifier.padding(12.dp).then(Modifier.clearAndSetSemantics {}))
                    Text(stringResource(label), style = MaterialTheme.typography.bodyMedium)
                }
            }
            Spacer(Modifier.height(16.dp))
            BasicTextField(
                value = uiState.userMessage,
                onValueChange = onUserMessageUpdate,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp).onFocusChanged { messageFocused = it.isFocused },
                decorationBox = { inner ->
                    Column {
                        Box(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                            if (uiState.userMessage.isEmpty()) Text(stringResource(R.string.report_detail), color = MaterialTheme.colorScheme.outline)
                            inner()
                        }
                        HorizontalDivider(color = if (messageFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline)
                    }
                },
            )
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
