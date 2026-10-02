package com.illusion.checkfirm.feature.sherlock

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.text.style.TextAlign
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
import androidx.compose.foundation.shape.CircleShape
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
import com.illusion.checkfirm.core.designsystem.component.OneCard
import com.illusion.checkfirm.core.designsystem.component.OneIcons
import com.illusion.checkfirm.core.designsystem.component.OneNavButton
import com.illusion.checkfirm.core.designsystem.component.OneScaffold
import com.illusion.checkfirm.core.designsystem.component.OneTab
import com.illusion.checkfirm.core.designsystem.preview.ScreenPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.feature.sherlock.util.SherlockStatus
import com.illusion.checkfirm.feature.sherlock.R as FeatureR

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
        title = stringResource(FeatureR.string.sherlock),
        expandable = false,
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
        actions = {
            IconButton(onClick = onShowInfo) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = stringResource(R.string.help),
                    tint = CheckFirmTheme.colors.toolbarIconTint,
                )
            }
        },
    ) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(bottom = innerPadding.calculateBottomPadding())) {
            BoxWithConstraints(Modifier.weight(1f).fillMaxWidth()) {
            val minimumHeight = maxHeight - 24.dp
            Column(Modifier.fillMaxWidth().padding(12.dp).verticalScroll(rememberScrollState()).heightIn(min = minimumHeight),
                horizontalAlignment = Alignment.CenterHorizontally) {
                StatusFace(uiState.status)
                Spacer(Modifier.height(12.dp))
                if (uiState.status == SherlockStatus.INITIAL) Text(
                    stringResource(if (uiState.selectedTab == 0) FeatureR.string.sherlock_manual_description else FeatureR.string.sherlock_script_description),
                    style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                ) else StatusBanner(uiState.status)
                if (uiState.status == SherlockStatus.SUCCESS && uiState.decryptedFirmware.isNotBlank()) {
                    Spacer(Modifier.height(32.dp))
                    SuccessCard(uiState.decryptedFirmware) { copyToClipboard(context, uiState.decryptedFirmware) }
                }
                Spacer(Modifier.height(24.dp))
                // Keep the controls near the bottom as in the XML fragments, while allowing short windows to scroll.
                Spacer(Modifier.weight(1f))
                if (uiState.selectedTab == 0) ManualTab(uiState, onBuildPrefixChange, onCscPrefixChange, onBasebandPrefixChange,
                    onManualBuildChange, onManualCscChange, onManualBasebandChange)
                else ScriptTab(uiState.scriptStart, uiState.scriptEnd, uiState.status == SherlockStatus.RUNNING,
                    onScriptStartChange, onScriptEndChange, onStartScript)
            }
            }
            OneTab(listOf(stringResource(FeatureR.string.sherlock_tab_manual), stringResource(FeatureR.string.sherlock_tab_script)), uiState.selectedTab, onTabChange)
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
    androidx.compose.ui.viewinterop.AndroidView(
        factory = { android.widget.ImageView(it) },
        modifier = Modifier.size(120.dp),
        update = { image ->
            if (image.tag != drawable) {
                (image.drawable as? android.graphics.drawable.Animatable)?.stop()
                image.setImageDrawable(image.context.getDrawable(drawable))
                image.tag = drawable
                (image.drawable as? android.graphics.drawable.Animatable)?.start()
            }
        },
    )
}

@Composable
private fun StatusBanner(status: SherlockStatus) {
    val message = when (status) {
        SherlockStatus.SUCCESS -> stringResource(FeatureR.string.sherlock_result_success)
        SherlockStatus.FAIL -> stringResource(FeatureR.string.sherlock_result_fail)
        SherlockStatus.RUNNING -> stringResource(FeatureR.string.sherlock_script_running)
        SherlockStatus.NO_WARNING -> stringResource(FeatureR.string.sherlock_script_no_error)
        SherlockStatus.WARNING_BUILD_NUMBER_BOOTLOADER -> stringResource(FeatureR.string.sherlock_script_error_1)
        SherlockStatus.WARNING_BUILD_NUMBER_ONE_UI_VERSION -> stringResource(FeatureR.string.sherlock_script_error_3)
        SherlockStatus.WARNING_BUILD_NUMBER_YEAR -> stringResource(FeatureR.string.sherlock_script_error_4)
        SherlockStatus.WARNING_BUILD_NUMBER_MONTH -> stringResource(FeatureR.string.sherlock_script_error_5)
        SherlockStatus.WARNING_BUILD_NUMBER_REVISION -> stringResource(FeatureR.string.sherlock_script_error_6)
        SherlockStatus.WARNING_SCRIPT_START_INVALID -> stringResource(FeatureR.string.sherlock_script_error_7)
        SherlockStatus.WARNING_SCRIPT_END_INVALID -> stringResource(FeatureR.string.sherlock_script_error_8)
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
        style = MaterialTheme.typography.titleMedium,
        color = color,
        textAlign = TextAlign.Center,
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
    PrefixSplitField(
        label = stringResource(R.string.sherlock_build),
        prefix = state.buildPrefix,
        body = state.manualBuild,
        bodyMaxLen = 6,
        status = state.status,
        onPrefixChange = onBuildPrefixChange,
        onBodyChange = onManualBuildChange,
    )
    Spacer(Modifier.height(8.dp))
    PrefixSplitField(
        label = "CSC",
        prefix = state.cscPrefix,
        body = state.manualCsc,
        bodyMaxLen = 5,
        status = state.status,
        onPrefixChange = onCscPrefixChange,
        onBodyChange = onManualCscChange,
    )
    Spacer(Modifier.height(8.dp))
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
    OneCard {
        Column(Modifier.padding(12.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(start, { if (it.length <= 6) onStartChange(it.uppercase()) },
                    label = { Text(stringResource(FeatureR.string.sherlock_script_start_value)) },
                    singleLine = true, modifier = Modifier.weight(1f))
                OutlinedTextField(end, { if (it.length <= 6) onEndChange(it.uppercase()) },
                    label = { Text(stringResource(FeatureR.string.sherlock_script_end_value)) },
                    singleLine = true, modifier = Modifier.weight(1f))
            }
            Spacer(Modifier.height(8.dp))
            Button(onClick = onStart, enabled = !running, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(FeatureR.string.sherlock_script_start))
            }
        }
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
                BasicTextField(prefix, { onPrefixChange(it.uppercase()) },
                    singleLine = true, textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
                    modifier = Modifier.width(IntrinsicSize.Min).widthIn(min = 32.dp).padding(vertical = 4.dp))
                BasicTextField(body, { if (it.length <= bodyMaxLen) onBodyChange(it.uppercase()) },
                    singleLine = true, textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
                    modifier = Modifier.weight(1f).padding(start = 2.dp, top = 4.dp, bottom = 4.dp))
            }
        }
    }
}

@Composable
private fun SimpleField(label: String, value: String, onChange: (String) -> Unit) {
    OneCard(
        modifier = Modifier.fillMaxWidth(),
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
