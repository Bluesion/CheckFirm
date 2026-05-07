package com.illusion.checkfirm.feature.sherlock

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneIcons
import com.illusion.checkfirm.core.designsystem.component.OneScaffold
import com.illusion.checkfirm.core.designsystem.component.OneTab
import com.illusion.checkfirm.core.designsystem.preview.ScreenPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.feature.sherlock.util.SherlockStatus

@Composable
fun SherlockScreen(
    uiState: SherlockUiState = SherlockUiState(),
    onNavigationIconClick: () -> Unit,
    onTabChange: (Int) -> Unit = {},
    onBuildPrefixChange: (String) -> Unit = {},
    onCscPrefixChange: (String) -> Unit = {},
    onBasebandPrefixChange: (String) -> Unit = {},
    onManualBuildChange: (String) -> Unit = {},
    onManualCscChange: (String) -> Unit = {},
    onManualBasebandChange: (String) -> Unit = {},
    onScriptStartChange: (String) -> Unit = {},
    onScriptEndChange: (String) -> Unit = {},
    onStartScript: () -> Unit = {},
    onShowInfo: () -> Unit = {},
    onDismissInfo: () -> Unit = {},
) {
    val context = LocalContext.current

    OneScaffold(
        title = stringResource(R.string.sherlock),
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    imageVector = OneIcons.IcBack,
                    contentDescription = null,
                    tint = CheckFirmTheme.colors.toolbarIconTint,
                )
            }
        },
        actions = {
            IconButton(onClick = onShowInfo) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = null,
                    tint = CheckFirmTheme.colors.toolbarIconTint,
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(bottom = innerPadding.calculateBottomPadding())
                .fillMaxSize(),
        ) {
            OneTab(
                titles = listOf(
                    stringResource(R.string.sherlock_tab_manual),
                    stringResource(R.string.sherlock_tab_script),
                ),
                selectedTabIndex = uiState.selectedTab,
                onTabSelected = onTabChange,
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 12.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                StatusFace(status = uiState.status)
                StatusBanner(status = uiState.status)

                if (uiState.status == SherlockStatus.SUCCESS && uiState.decryptedFirmware.isNotBlank()) {
                    SuccessCard(
                        firmware = uiState.decryptedFirmware,
                        onCopy = { copyToClipboard(context, uiState.decryptedFirmware) },
                    )
                }

                when (uiState.selectedTab) {
                    0 -> ManualTab(
                        state = uiState,
                        onBuildPrefixChange = onBuildPrefixChange,
                        onCscPrefixChange = onCscPrefixChange,
                        onBasebandPrefixChange = onBasebandPrefixChange,
                        onManualBuildChange = onManualBuildChange,
                        onManualCscChange = onManualCscChange,
                        onManualBasebandChange = onManualBasebandChange,
                    )

                    else -> ScriptTab(
                        start = uiState.scriptStart,
                        end = uiState.scriptEnd,
                        running = uiState.status == SherlockStatus.RUNNING,
                        onStartChange = onScriptStartChange,
                        onEndChange = onScriptEndChange,
                        onStart = onStartScript,
                    )
                }
            }
        }
    }

    if (uiState.showInfoDialog) {
        SherlockInformationDialog(onDismiss = onDismissInfo)
    }
}

@Composable
private fun StatusFace(status: SherlockStatus) {
    val drawable = when (status) {
        SherlockStatus.SUCCESS -> R.drawable.ic_sherlock_success_face
        SherlockStatus.FAIL -> R.drawable.ic_sherlock_fail_face
        SherlockStatus.RUNNING -> R.drawable.ic_sherlock_loading_face
        SherlockStatus.WARNING_SCRIPT_START_INVALID,
        SherlockStatus.WARNING_SCRIPT_END_INVALID,
        SherlockStatus.WARNING_BUILD_NUMBER_BOOTLOADER,
        SherlockStatus.WARNING_BUILD_NUMBER_ONE_UI_VERSION,
        SherlockStatus.WARNING_BUILD_NUMBER_YEAR,
        SherlockStatus.WARNING_BUILD_NUMBER_MONTH,
        SherlockStatus.WARNING_BUILD_NUMBER_REVISION -> R.drawable.ic_sherlock_warning_face

        else -> R.drawable.ic_sherlock_normal_face
    }
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Image(
            painter = painterResource(drawable),
            contentDescription = null,
            modifier = Modifier.size(96.dp),
        )
    }
}

@Composable
private fun StatusBanner(status: SherlockStatus) {
    val message = when (status) {
        SherlockStatus.SUCCESS -> stringResource(R.string.sherlock_result_success)
        SherlockStatus.FAIL -> stringResource(R.string.sherlock_result_fail)
        SherlockStatus.RUNNING -> stringResource(R.string.sherlock_script_running)
        SherlockStatus.NO_WARNING -> stringResource(R.string.sherlock_script_no_error)
        SherlockStatus.WARNING_BUILD_NUMBER_BOOTLOADER -> stringResource(R.string.sherlock_script_error_1)
        SherlockStatus.WARNING_BUILD_NUMBER_ONE_UI_VERSION -> stringResource(R.string.sherlock_script_error_3)
        SherlockStatus.WARNING_BUILD_NUMBER_YEAR -> stringResource(R.string.sherlock_script_error_4)
        SherlockStatus.WARNING_BUILD_NUMBER_MONTH -> stringResource(R.string.sherlock_script_error_5)
        SherlockStatus.WARNING_BUILD_NUMBER_REVISION -> stringResource(R.string.sherlock_script_error_6)
        SherlockStatus.WARNING_SCRIPT_START_INVALID -> stringResource(R.string.sherlock_script_error_7)
        SherlockStatus.WARNING_SCRIPT_END_INVALID -> stringResource(R.string.sherlock_script_error_8)
        SherlockStatus.INITIAL -> return
    }
    val color = when (status) {
        SherlockStatus.SUCCESS, SherlockStatus.NO_WARNING -> Color(0xFF2E7D32)
        SherlockStatus.FAIL -> MaterialTheme.colorScheme.error
        SherlockStatus.RUNNING -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.tertiary
    }
    Text(
        text = message,
        style = MaterialTheme.typography.bodyMedium,
        color = color,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun SuccessCard(firmware: String, onCopy: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = firmware,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.weight(1f),
            )
            IconButton(onClick = onCopy) {
                Icon(
                    imageVector = Icons.Outlined.ContentCopy,
                    contentDescription = stringResource(android.R.string.copy),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
    }
}

@Composable
private fun ManualTab(
    state: SherlockUiState,
    onBuildPrefixChange: (String) -> Unit,
    onCscPrefixChange: (String) -> Unit,
    onBasebandPrefixChange: (String) -> Unit,
    onManualBuildChange: (String) -> Unit,
    onManualCscChange: (String) -> Unit,
    onManualBasebandChange: (String) -> Unit,
) {
    Text(
        text = stringResource(R.string.sherlock_manual_description),
        style = MaterialTheme.typography.bodyMedium,
    )
    PrefixSplitField(
        label = stringResource(R.string.sherlock_build),
        prefix = state.buildPrefix,
        body = state.manualBuild,
        bodyMaxLen = 6,
        status = state.status,
        onPrefixChange = onBuildPrefixChange,
        onBodyChange = onManualBuildChange,
    )
    PrefixSplitField(
        label = "CSC",
        prefix = state.cscPrefix,
        body = state.manualCsc,
        bodyMaxLen = 6,
        status = state.status,
        onPrefixChange = onCscPrefixChange,
        onBodyChange = onManualCscChange,
    )
    PrefixSplitField(
        label = stringResource(R.string.sherlock_baseband),
        prefix = state.basebandPrefix,
        body = state.manualBaseband,
        bodyMaxLen = 6,
        status = state.status,
        onPrefixChange = onBasebandPrefixChange,
        onBodyChange = onManualBasebandChange,
    )
}

@Composable
private fun ScriptTab(
    start: String,
    end: String,
    running: Boolean,
    onStartChange: (String) -> Unit,
    onEndChange: (String) -> Unit,
    onStart: () -> Unit,
) {
    Text(
        text = stringResource(R.string.sherlock_script_description),
        style = MaterialTheme.typography.bodyMedium,
    )
    SimpleField(
        label = stringResource(R.string.sherlock_script_start_value),
        value = start,
        onChange = onStartChange,
    )
    SimpleField(
        label = stringResource(R.string.sherlock_script_end_value),
        value = end,
        onChange = onEndChange,
    )
    Button(
        onClick = onStart,
        enabled = !running,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(text = stringResource(R.string.sherlock_script_start))
    }
}

@Composable
private fun PrefixSplitField(
    label: String,
    prefix: String,
    body: String,
    bodyMaxLen: Int,
    status: SherlockStatus,
    onPrefixChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
) {
    val borderColor = when (status) {
        SherlockStatus.SUCCESS -> Color(0xFF2E7D32)
        SherlockStatus.FAIL -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.outline
    }
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = prefix,
                    onValueChange = { onPrefixChange(it.uppercase()) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
                    modifier = Modifier.weight(1f),
                )
                Spacer(Modifier.width(8.dp))
                OutlinedTextField(
                    value = body,
                    onValueChange = { if (it.length <= bodyMaxLen) onBodyChange(it.uppercase()) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun SimpleField(label: String, value: String, onChange: (String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
            )
            OutlinedTextField(
                value = value,
                onValueChange = { if (it.length <= 6) onChange(it.uppercase()) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

private fun copyToClipboard(context: Context, text: String) {
    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    cm.setPrimaryClip(ClipData.newPlainText("CheckFirm", text))
}

@ScreenPreview
@Composable
private fun SherlockScreenPreview() {
    CheckFirmTheme {
        Surface { SherlockScreen(onNavigationIconClick = {}) }
    }
}

@ScreenPreview
@Composable
private fun SherlockScreenSuccessPreview() {
    CheckFirmTheme {
        Surface {
            SherlockScreen(
                uiState = SherlockUiState(
                    status = SherlockStatus.SUCCESS,
                    decryptedFirmware = "S928BXXSAGW1/S928BOXMAGW1/S928BXXSAGW1",
                    buildPrefix = "S928BXXS",
                    manualBuild = "AGW1",
                ),
                onNavigationIconClick = {},
            )
        }
    }
}
