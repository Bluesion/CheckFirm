package com.illusion.checkfirm.feature.welcome

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.preview.ScreenPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

// Domain Mock for UI representation
data class WelcomeSearchDevice(val model: String, val csc: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeSearchScreen(
    uiState: WelcomeSearchUiState = WelcomeSearchUiState(),
    onIsWelcomeSearchEnabledChange: (Boolean) -> Unit = {},
    onShowDialogChange: (Boolean) -> Unit = {},
    onModelChange: (String) -> Unit = {},
    onCscChange: (String) -> Unit = {},
    onSelectedChipChange: (String?) -> Unit = {},
    onNavigationIconClick: () -> Unit = {},
) {
    val devices = remember { mutableStateListOf<WelcomeSearchDevice>() }

    if (uiState.showDialog) {
        WelcomeSearchDialog(
            uiState = uiState,
            onModelChange = onModelChange,
            onCscChange = onCscChange,
            onSelectedChipChange = onSelectedChipChange,
            onDismissRequest = { onShowDialogChange(false) },
            onAddDevice = { model, csc ->
                devices.add(WelcomeSearchDevice(model, csc))
                onShowDialogChange(false)
            }
        )
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.welcome_search)) },
                navigationIcon = {
                    IconButton(onClick = onNavigationIconClick) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Switch Card (com.bluesion.oneui.switchcard.OneUISwitchCard equivalent)
            Card(
                onClick = { onIsWelcomeSearchEnabledChange(!uiState.isWelcomeSearchEnabled) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.welcome_search),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Switch(
                        checked = uiState.isWelcomeSearchEnabled,
                        onCheckedChange = { onIsWelcomeSearchEnabledChange(it) }
                    )
                }
            }

            Text(
                text = stringResource(R.string.welcome_search_description),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .padding(horizontal = 12.dp)
            )

            if (devices.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    shape = MaterialTheme.shapes.large
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        devices.forEach { device ->
                            WelcomeSearchItem(
                                device = device,
                                onDelete = { devices.remove(device) }
                            )
                        }
                    }
                }

                if (devices.size < 5) {
                    Button(
                        onClick = { onShowDialogChange(true) },
                        modifier = Modifier
                            .padding(top = 48.dp)
                            .align(Alignment.CenterHorizontally),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Icon(Icons.Rounded.Add, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.welcome_search_add_device))
                    }

                    Text(
                        text = stringResource(R.string.welcome_search_empty_device_list),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(top = 4.dp)
                    )
                }
            } else {
                // Empty state
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 48.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { onShowDialogChange(true) },
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Icon(Icons.Rounded.Add, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.welcome_search_add_device))
                    }

                    Text(
                        text = stringResource(R.string.welcome_search_empty_device_list),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun WelcomeSearchItem(
    device: WelcomeSearchDevice,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .padding(start = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${device.model} (${device.csc})",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
            maxLines = 1
        )

        IconButton(
            onClick = onDelete,
            modifier = Modifier.padding(end = 4.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = "Delete"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeSearchDialog(
    uiState: WelcomeSearchUiState,
    onModelChange: (String) -> Unit,
    onCscChange: (String) -> Unit,
    onSelectedChipChange: (String?) -> Unit,
    onDismissRequest: () -> Unit,
    onAddDevice: (model: String, csc: String) -> Unit,
) {
    // Sample mock data for ChipGroup based on XML intent
    val chips = listOf("S24", "S23", "Z Fold5")
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.welcome_search),
                    style = MaterialTheme.typography.headlineSmall
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    chips.forEach { chip ->
                        FilterChip(
                            selected = chip == uiState.selectedChip,
                            onClick = { onSelectedChipChange(chip) },
                            label = { Text(chip) }
                        )
                    }
                }

                OutlinedTextField(
                    value = uiState.model,
                    onValueChange = { onModelChange(it.uppercase()) },
                    label = { Text(stringResource(R.string.model)) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                )

                OutlinedTextField(
                    value = uiState.csc,
                    onValueChange = { if (it.length <= 3) onCscChange(it.uppercase()) },
                    label = { Text(stringResource(R.string.csc)) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text(stringResource(android.R.string.cancel))
                    }
                    Spacer(Modifier.width(8.dp))
                    TextButton(
                        onClick = { onAddDevice(uiState.model, uiState.csc) },
                        enabled = uiState.model.isNotBlank() && uiState.csc.length == 3
                    ) {
                        Text(stringResource(R.string.add_item))
                    }
                }
            }
        }
    }
}

@Composable
fun WelcomeSearchRoute(
    viewModel: WelcomeSearchViewModel = hiltViewModel(),
    onNavigationIconClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    WelcomeSearchScreen(
        uiState = uiState,
        onIsWelcomeSearchEnabledChange = viewModel::updateIsWelcomeSearchEnabled,
        onShowDialogChange = viewModel::updateShowDialog,
        onModelChange = viewModel::updateModel,
        onCscChange = viewModel::updateCsc,
        onSelectedChipChange = viewModel::updateSelectedChip,
        onNavigationIconClick = onNavigationIconClick
    )
}

@ScreenPreview
@Composable
private fun WelcomeSearchScreenPreview() {
    CheckFirmTheme {
        WelcomeSearchScreen(
            onNavigationIconClick = {},
        )
    }
}
