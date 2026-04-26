@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.illusion.checkfirm.feature.report

import android.widget.Toast
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneIcons
import com.illusion.checkfirm.core.designsystem.component.OneScaffold
import com.illusion.checkfirm.core.designsystem.preview.ScreenPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

@Composable
fun ReportRoute(
    onNavigationIconClick: () -> Unit,
    viewModel: ReportViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(viewModel.events) {
        viewModel.events.collect { event ->
            when (event) {
                is ReportEvent.SubmitSuccess -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is ReportEvent.SubmitError -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    ReportScreen(
        uiState = uiState,
        onBugTypeChange = viewModel::updateBugType,
        onDeviceDetailsChange = viewModel::updateDeviceDetails,
        onLogsChange = viewModel::updateLogs,
        onConsentChange = viewModel::updateConsent,
        onSubmitClick = viewModel::submitReport,
        onNavigationIconClick = onNavigationIconClick
    )
}

@Composable
fun ReportScreen(
    uiState: ReportUiState,
    onBugTypeChange: (String) -> Unit,
    onDeviceDetailsChange: (String) -> Unit,
    onLogsChange: (String) -> Unit,
    onConsentChange: (Boolean) -> Unit,
    onSubmitClick: () -> Unit,
    onNavigationIconClick: () -> Unit,
) {
    OneScaffold(
        title = stringResource(R.string.report),
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    imageVector = OneIcons.IcBack,
                    contentDescription = null,
                    tint = CheckFirmTheme.colors.toolbarIconTint,
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(top = 12.dp, bottom = innerPadding.calculateBottomPadding() + 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
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

            ReportTypeRow(
                title = stringResource(R.string.report_type_1),
                checked = uiState.bugType == "type_1",
                onCheckedChange = { if (it) onBugTypeChange("type_1") else onBugTypeChange("") },
            )
            ReportTypeRow(
                title = stringResource(R.string.report_type_2),
                checked = uiState.bugType == "type_2",
                onCheckedChange = { if (it) onBugTypeChange("type_2") else onBugTypeChange("") },
            )
            ReportTypeRow(
                title = stringResource(R.string.report_type_3),
                checked = uiState.bugType == "type_3",
                onCheckedChange = { if (it) onBugTypeChange("type_3") else onBugTypeChange("") },
            )
            ReportTypeRow(
                title = stringResource(R.string.report_type_4),
                checked = uiState.bugType == "type_4",
                onCheckedChange = { if (it) onBugTypeChange("type_4") else onBugTypeChange("") },
            )

            OutlinedTextField(
                value = uiState.logs,
                onValueChange = onLogsChange,
                placeholder = { Text(stringResource(R.string.report_detail)) },
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
                enabled = uiState.bugType.isNotBlank() && !uiState.isSubmitting,
            ) {
                if (uiState.isSubmitting) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text(stringResource(R.string.report_submit))
                }
            }
        }
    }
}

@Composable
private fun ReportTypeRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(vertical = 4.dp),
    ) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
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
        ReportScreen(
            uiState = ReportUiState(),
            onBugTypeChange = {},
            onDeviceDetailsChange = {},
            onLogsChange = {},
            onConsentChange = {},
            onSubmitClick = {},
            onNavigationIconClick = {},
        )
    }
}
