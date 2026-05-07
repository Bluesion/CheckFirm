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
import androidx.compose.material3.Surface
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
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

private data class LanguageEntry(val tag: String, val labelRes: Int)

private val LANGUAGES = listOf(
    LanguageEntry("", R.string.settings_language_default),
    LanguageEntry("cs", R.string.settings_language_cs),
    LanguageEntry("en-US", R.string.settings_language_en),
    LanguageEntry("ja", R.string.settings_language_ja),
    LanguageEntry("ko", R.string.settings_language_ko),
    LanguageEntry("pt-BR", R.string.settings_language_pt_rBR),
    LanguageEntry("pt-PT", R.string.settings_language_pt_rPT),
    LanguageEntry("ro", R.string.settings_language_ro),
    LanguageEntry("ru", R.string.settings_language_ru),
    LanguageEntry("si", R.string.settings_language_si),
    LanguageEntry("tr", R.string.settings_language_tr),
    LanguageEntry("zh-Hans", R.string.settings_language_zh_rCN),
    LanguageEntry("zh-Hant", R.string.settings_language_zh_rTW),
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
            LANGUAGES.forEach { entry ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { current = entry.tag }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(entry.labelRes),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f),
                    )
                    OneRadioButton(
                        selected = current == entry.tag,
                        onClick = { current = entry.tag },
                    )
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
            Text(text = stringResource(android.R.string.ok))
        }
    }
}

@ComponentPreview
@Composable
private fun LanguageDialogPreview() {
    CheckFirmTheme {
        Surface {
            LanguageDialog(
                selectedLanguage = "en-US",
                onDismiss = {},
                onConfirm = {},
            )
        }
    }
}
