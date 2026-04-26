package com.illusion.checkfirm.feature.catcher.impl.presentation

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneIcons
import com.illusion.checkfirm.core.designsystem.component.OneScaffold
import com.illusion.checkfirm.core.designsystem.component.OneSwitchCard
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoCatcherScreen(
    uiState: InfoCatcherUiState,
    onNavigationIconClick: () -> Unit,
    onEnableChange: (Boolean) -> Unit,
    onAddDeviceClick: () -> Unit,
    onDeleteDevice: (com.illusion.checkfirm.domain.model.Device) -> Unit,
    onDialogDismiss: () -> Unit,
    onAddDevice: (String, String) -> Unit,
) {
    OneScaffold(
        title = stringResource(R.string.info_catcher),
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
                .padding(bottom = innerPadding.calculateBottomPadding())
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            OneSwitchCard(
                checked = uiState.isEnabled,
                onCheckedChange = onEnableChange,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Info catcher periodically checks for firmware updates of saved devices in background and notifies you.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(top = 16.dp, bottom = 24.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            ) {
                Column {
                    if (uiState.devices.isEmpty()) {
                        Text(
                            text = "No saved devices",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        uiState.devices.forEach { device ->
                            InfoCatcherItem(
                                deviceText = "${device.model} / ${device.csc}",
                                onDelete = { onDeleteDevice(device) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = onAddDeviceClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Icon(Icons.Rounded.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Add device")
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (uiState.showDialog) {
        InfoCatcherDialog(
            onDismissRequest = onDialogDismiss,
            onAdd = onAddDevice
        )
    }
}

@Composable
fun InfoCatcherItem(
    deviceText: String,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = deviceText,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
            maxLines = 1
        )

        IconButton(
            onClick = onDelete,
            modifier = Modifier
                .size(48.dp)
                .padding(12.dp)
                .clip(CircleShape)
        ) {
            Icon(Icons.Rounded.Close, contentDescription = "Delete")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoCatcherDialog(
    onDismissRequest: () -> Unit,
    onAdd: (String, String) -> Unit
) {
    var model by remember { mutableStateOf("SM-") }
    var csc by remember { mutableStateOf("") }

    val chipScrollState = rememberScrollState()

    ModalBottomSheet(
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = "Info Catcher",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(chipScrollState),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Galaxy S", "Galaxy Z", "Galaxy A", "Galaxy Tab").forEach { series ->
                    FilterChip(
                        selected = false,
                        onClick = {
                            when (series) {
                                "Galaxy S" -> model = "SM-S9"
                                "Galaxy Z" -> model = "SM-F"
                                "Galaxy A" -> model = "SM-A"
                                "Galaxy Tab" -> model = "SM-X"
                            }
                        },
                        label = { Text(series) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = model,
                onValueChange = { model = it.uppercase() },
                label = { Text(stringResource(R.string.model)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = csc,
                onValueChange = {
                    if (it.length <= 3) csc = it.uppercase()
                },
                label = { Text(stringResource(R.string.csc)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismissRequest) {
                    Text(stringResource(android.R.string.cancel))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { onAdd(model, csc) }) {
                    Text(stringResource(R.string.add_item))
                }
            }
        }
    }
}

