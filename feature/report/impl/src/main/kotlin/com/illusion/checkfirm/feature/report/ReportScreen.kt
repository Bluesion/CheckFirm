package com.illusion.checkfirm.feature.report

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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

@OptIn(ExperimentalMaterial3Api::class)
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
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.report_bug_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigationIconClick) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Please describe the issue you encountered. Provide as much detail as possible.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )

            OutlinedTextField(
                value = uiState.bugType,
                onValueChange = onBugTypeChange,
                label = { Text(stringResource(R.string.type_of_bug_text)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.deviceDetails,
                onValueChange = onDeviceDetailsChange,
                label = { Text(stringResource(R.string.device_details_text)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = uiState.logs,
                onValueChange = onLogsChange,
                label = { Text(stringResource(R.string.logs_steps_text)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 4
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = uiState.consentGiven,
                    onCheckedChange = onConsentChange
                )
                Text(
                    text = "I consent to sharing this data for debugging purposes.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Button(
                onClick = onSubmitClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.consentGiven && uiState.bugType.isNotBlank() && uiState.deviceDetails.isNotBlank() && uiState.logs.isNotBlank() && !uiState.isSubmitting
            ) {
                if (uiState.isSubmitting) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text(stringResource(R.string.submit_report_text))
                }
            }
        }
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
