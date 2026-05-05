package com.illusion.checkfirm.feature.settings.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

@Composable
fun ContributorDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.contributor)) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ContributorGroup(
                    title = R.string.contributor_application,
                    names = listOf("Illusion")
                )
                ContributorGroup(title = R.string.contributor_designer, names = listOf("Illusion"))
                ContributorGroup(title = R.string.contributor_developer, names = listOf("Illusion"))
                ContributorGroup(
                    title = R.string.contributor_translator, names = listOf(
                        "Russian", "Chinese", "Czech", "Portuguese", "Romanian", "Sinhala"
                    )
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text(text = stringResource(R.string.close)) }
        }
    )
}

@Composable
private fun ContributorGroup(title: Int, names: List<String>) {
    Text(
        text = stringResource(title),
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary
    )
    names.forEach { name ->
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@ComponentPreview
@Composable
private fun ContributorDialogPreview() {
    CheckFirmTheme {
        Surface {
            ContributorDialog(onDismiss = {})
        }
    }
}
