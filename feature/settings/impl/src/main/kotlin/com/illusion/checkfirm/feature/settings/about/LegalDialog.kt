package com.illusion.checkfirm.feature.settings.about

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TextButton
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

@Composable
fun LegalDialog(onDismiss: () -> Unit) {
    val rawText = stringResource(FeatureR.string.legal_text)
    val linkColor = MaterialTheme.colorScheme.primary
    val linked = remember(rawText, linkColor) { rawText.linkify(linkColor) }

    OneBottomSheetDialog(
        title = stringResource(FeatureR.string.legal),
        onDismiss = onDismiss,
    ) {
        Text(
            text = linked,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth(),
        )
        Spacer(Modifier.height(16.dp))
        TextButton(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
        ) {
            Text(text = stringResource(android.R.string.ok), color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

private fun String.linkify(linkColor: Color): AnnotatedString = buildAnnotatedString {
    val text = this@linkify
    val nativeLinks = android.text.SpannableString(text)
    android.text.util.Linkify.addLinks(nativeLinks, android.text.util.Linkify.WEB_URLS)
    var lastEnd = 0
    nativeLinks.getSpans(0, text.length, android.text.style.URLSpan::class.java)
        .sortedBy(nativeLinks::getSpanStart)
        .forEach { span ->
            val start = nativeLinks.getSpanStart(span)
            val end = nativeLinks.getSpanEnd(span)
            append(text.substring(lastEnd, start))
            withLink(
                LinkAnnotation.Url(
                    url = span.url,
                    styles = TextLinkStyles(
                        style = SpanStyle(color = linkColor, textDecoration = TextDecoration.Underline),
                    ),
                ),
            ) {
                append(text.substring(start, end))
            }
            lastEnd = end
        }
    append(text.substring(lastEnd))
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
