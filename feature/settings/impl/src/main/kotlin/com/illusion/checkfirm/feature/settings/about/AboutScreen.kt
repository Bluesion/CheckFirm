package com.illusion.checkfirm.feature.settings.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneIcons
import com.illusion.checkfirm.core.designsystem.component.OneScaffold
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.preview.ScreenPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

@Composable
fun AboutScreen(
    uiState: AboutUiState = AboutUiState(),
    onNavigateBack: () -> Unit,
    showDialog: (DialogType) -> Unit,
    hideDialog: () -> Unit,
) {
    val context = LocalContext.current

    OneScaffold(
        title = "",
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = OneIcons.IcBack,
                    contentDescription = null,
                    tint = CheckFirmTheme.colors.toolbarIconTint,
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .padding(bottom = innerPadding.calculateBottomPadding()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
            )

            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.about_version),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Spacer(Modifier.widthIn(min = 4.dp))
                Text(
                    text = " " + context.versionName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.about_latest),
                style = MaterialTheme.typography.bodyMedium,
                color = CheckFirmTheme.colors.settingsDescription,
            )

            Spacer(Modifier.weight(1f))

            AboutPageButton(
                text = stringResource(R.string.contributor),
                onClick = { showDialog(DialogType.CONTRIBUTOR) },
            )
            Spacer(Modifier.height(8.dp))
            AboutPageButton(
                text = stringResource(R.string.legal),
                onClick = { showDialog(DialogType.LEGAL) },
            )
        }
    }

    when (uiState.activeDialog) {
        DialogType.CONTRIBUTOR -> {
            ContributorDialog(
                onDismiss = hideDialog,
            )
        }

        DialogType.LEGAL -> {
            LegalDialog(
                onDismiss = hideDialog,
            )
        }

        else -> Unit
    }
}

@Composable
private fun AboutPageButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(22.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = CheckFirmTheme.colors.aboutPageButtonBackground,
            contentColor = CheckFirmTheme.colors.aboutPageButtonText,
        ),
        modifier = Modifier
            .widthIn(min = 216.dp)
            .height(50.dp)
            .padding(horizontal = 16.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
            ),
            maxLines = 1,
        )
    }
}

@ScreenPreview
@Composable
private fun AboutScreenPreview() {
    CheckFirmTheme {
        Surface {
            AboutScreen(
                onNavigateBack = {},
                showDialog = {},
                hideDialog = {},
            )
        }
    }
}

@ComponentPreview
@Composable
private fun AboutPageButtonPreview() {
    CheckFirmTheme {
        Surface {
            AboutPageButton(
                text = "Contributor",
                onClick = {},
            )
        }
    }
}
