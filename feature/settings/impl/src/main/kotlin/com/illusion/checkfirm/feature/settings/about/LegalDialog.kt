package com.illusion.checkfirm.feature.settings.about

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.component.OneBottomSheetDialog
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.feature.settings.R as FeatureR

private val URL_PATTERN = Regex("https?://[^\\s]+")

@Composable
fun LegalDialog(onDismiss: () -> Unit) {
    val rawText = stringResource(FeatureR.string.legal_text)
    val linked = remember(rawText) { rawText.linkify() }

    OneBottomSheetDialog(
        title = stringResource(FeatureR.string.legal),
        onDismiss = onDismiss,
    ) {
        Text(
            text = linked,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
        )
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

private fun String.linkify(): AnnotatedString = buildAnnotatedString {
    val text = this@linkify
    var lastEnd = 0
    URL_PATTERN.findAll(text).forEach { match ->
        if (match.range.first > lastEnd) {
            append(text.substring(lastEnd, match.range.first))
        }
        withLink(
            LinkAnnotation.Url(
                url = match.value,
                styles = TextLinkStyles(
                    style = SpanStyle(textDecoration = TextDecoration.Underline),
                ),
            ),
        ) {
            append(match.value)
        }
        lastEnd = match.range.last + 1
    }
    if (lastEnd < text.length) {
        append(text.substring(lastEnd))
    }
}

@ComponentPreview
@Composable
private fun LegalDialogPreview() {
    CheckFirmTheme {
        Surface {
            LegalDialog(onDismiss = {})
        }
    }
}
