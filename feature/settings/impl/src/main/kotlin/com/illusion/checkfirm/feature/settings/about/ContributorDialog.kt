package com.illusion.checkfirm.feature.settings.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneBottomSheetDialog
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

private data class ContributorRow(val roleRes: Int, val nameRes: Int)

private val APPLICATION = listOf(
    ContributorRow(R.string.contributor_designer, R.string.contributor_newfit),
    ContributorRow(R.string.contributor_developer, R.string.contributor_bluesion),
)

private val TRANSLATORS = listOf(
    ContributorRow(R.string.contributor_russian, R.string.contributor_russian_translator),
    ContributorRow(R.string.contributor_chinese, R.string.contributor_chinese_translator),
    ContributorRow(R.string.contributor_czech, R.string.contributor_czech_translator),
    ContributorRow(R.string.contributor_portuguese, R.string.contributor_portuguese_translator),
    ContributorRow(R.string.contributor_romanian, R.string.contributor_romanian_translator),
    ContributorRow(R.string.contributor_sinhala, R.string.contributor_sinhala_translator),
)

@Composable
fun ContributorDialog(onDismiss: () -> Unit) {
    OneBottomSheetDialog(
        title = stringResource(R.string.contributor),
        onDismiss = onDismiss,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            SectionHeader(stringResource(R.string.contributor_application))
            APPLICATION.forEach { row -> ContributorRow(row) }

            Spacer(Modifier.height(8.dp))

            SectionHeader(stringResource(R.string.contributor_translator))
            TRANSLATORS.forEach { row -> ContributorRow(row) }
        }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
        ) {
            Text(text = stringResource(android.R.string.ok))
        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
private fun ContributorRow(row: ContributorRow) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(row.roleRes),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(0.4f),
        )
        Text(
            text = stringResource(row.nameRes),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(0.6f),
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
