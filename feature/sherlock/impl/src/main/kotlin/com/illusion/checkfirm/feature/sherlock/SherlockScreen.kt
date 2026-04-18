package com.illusion.checkfirm.feature.sherlock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.illusion.checkfirm.core.designsystem.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SherlockScreen(
    uiState: SherlockUiState = SherlockUiState(),
    onNavigationIconClick: () -> Unit = {},
    onTabChange: (Int) -> Unit = {},
    onPdaChange: (String) -> Unit = {},
    onCscChange: (String) -> Unit = {},
    onBasebandChange: (String) -> Unit = {},
    onScriptStartChange: (String) -> Unit = {},
    onScriptEndChange: (String) -> Unit = {},
    onStartScript: () -> Unit = {},
    onDismissResult: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.sherlock)) },
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
                .fillMaxSize()
        ) {
            TabRow(selectedTabIndex = uiState.selectedTab) {
                Tab(
                    selected = uiState.selectedTab == 0,
                    onClick = { onTabChange(0) },
                    text = { Text(stringResource(R.string.sherlock_tab_manual)) }
                )
                Tab(
                    selected = uiState.selectedTab == 1,
                    onClick = { onTabChange(1) },
                    text = { Text(stringResource(R.string.sherlock_tab_script)) }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 12.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                when (uiState.selectedTab) {
                    0 -> ManualTab(
                        pda = uiState.pda,
                        csc = uiState.csc,
                        baseband = uiState.baseband,
                        onPdaChange = onPdaChange,
                        onCscChange = onCscChange,
                        onBasebandChange = onBasebandChange,
                    )

                    else -> ScriptTab(
                        start = uiState.scriptStart,
                        end = uiState.scriptEnd,
                        onStartChange = onScriptStartChange,
                        onEndChange = onScriptEndChange,
                        onStart = onStartScript,
                    )
                }
            }
        }
    }

    uiState.resultMessage?.let { message ->
        SherlockDialog(
            title = stringResource(R.string.sherlock),
            message = message,
            onDismiss = onDismissResult,
            onConfirm = onDismissResult,
        )
    }
}

@Composable
private fun ManualTab(
    pda: String,
    csc: String,
    baseband: String,
    onPdaChange: (String) -> Unit,
    onCscChange: (String) -> Unit,
    onBasebandChange: (String) -> Unit,
) {
    Text(
        text = stringResource(R.string.sherlock_manual_description),
        style = MaterialTheme.typography.bodyMedium
    )
    SherlockField(
        label = stringResource(R.string.sherlock_build),
        value = pda,
        onChange = onPdaChange
    )
    SherlockField(label = "CSC", value = csc, onChange = onCscChange)
    SherlockField(
        label = stringResource(R.string.sherlock_baseband),
        value = baseband,
        onChange = onBasebandChange
    )
}

@Composable
private fun ScriptTab(
    start: String,
    end: String,
    onStartChange: (String) -> Unit,
    onEndChange: (String) -> Unit,
    onStart: () -> Unit,
) {
    Text(
        text = stringResource(R.string.sherlock_script_description),
        style = MaterialTheme.typography.bodyMedium
    )
    SherlockField(
        label = stringResource(R.string.sherlock_script_start_value),
        value = start,
        onChange = onStartChange
    )
    SherlockField(
        label = stringResource(R.string.sherlock_script_end_value),
        value = end,
        onChange = onEndChange
    )
    Button(onClick = onStart, modifier = Modifier.fillMaxWidth()) {
        Text(stringResource(R.string.sherlock_script_start))
    }
}

@Composable
private fun SherlockField(label: String, value: String, onChange: (String) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            OutlinedTextField(
                value = value,
                onValueChange = onChange,
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun SherlockRoute(
    onNavigationIconClick: () -> Unit = {},
    viewModel: SherlockViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SherlockScreen(
        uiState = uiState,
        onNavigationIconClick = onNavigationIconClick,
        onTabChange = viewModel::selectTab,
        onPdaChange = viewModel::updatePda,
        onCscChange = viewModel::updateCsc,
        onBasebandChange = viewModel::updateBaseband,
        onScriptStartChange = viewModel::updateScriptStart,
        onScriptEndChange = viewModel::updateScriptEnd,
        onStartScript = { viewModel.setResult("Script completed") },
        onDismissResult = viewModel::clearResult,
    )
}
