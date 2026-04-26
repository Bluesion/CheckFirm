@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.illusion.checkfirm.feature.settings.language

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneBottomSheetDialog
import com.illusion.checkfirm.core.designsystem.component.OneRadioButton

private val LANGUAGES = listOf(
    "" to "System default",
    "en" to "English",
    "ko" to "한국어",
    "ja" to "日本語",
    "zh" to "中文",
    "ru" to "Русский",
    "cs" to "Čeština",
    "pt" to "Português",
    "ro" to "Română",
    "si" to "සිංහල",
    "tr" to "Türkçe",
)

@Composable
fun LanguageDialog(
    selectedLanguage: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var current by remember(selectedLanguage) { mutableStateOf(selectedLanguage) }

    OneBottomSheetDialog(
        title = stringResource(R.string.settings_language),
        onDismiss = onDismiss,
    ) {
        Spacer(Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
        ) {
            LANGUAGES.forEach { (code, label) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { current = code }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f),
                    )
                    OneRadioButton(selected = current == code, onClick = { current = code })
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                onConfirm(current)
                onDismiss()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
        ) {
            Text(stringResource(android.R.string.ok))
        }
    }
}
