package com.illusion.checkfirm.feature.settings.about

import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.widget.TextView
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.illusion.checkfirm.core.designsystem.component.OneBottomSheetDialog
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.core.designsystem.R as DesignR
import com.illusion.checkfirm.feature.settings.R as FeatureR

@Composable
fun LegalDialog(onDismiss: () -> Unit) {
    val rawText = stringResource(FeatureR.string.legal_text)
    val linkColor = MaterialTheme.colorScheme.primary.toArgb()
    val textColor = CheckFirmTheme.colors.textSecondary.toArgb()
    OneBottomSheetDialog(title = stringResource(FeatureR.string.legal), onDismiss = onDismiss) {
        // Retain TextView's URL line breaking, native link detection and accessibility.
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context -> TextView(context).apply {
                typeface = ResourcesCompat.getFont(context, DesignR.font.wanted_sans_medium)
                textSize = 14f
                movementMethod = LinkMovementMethod.getInstance()
            } },
            update = { view ->
                view.setTextColor(textColor)
                view.setLinkTextColor(linkColor)
                if (view.text.toString() != rawText) {
                    view.text = rawText
                    Linkify.addLinks(view, Linkify.WEB_URLS)
                }
            },
        )
        Spacer(Modifier.height(16.dp))
        TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(22.dp)) {
            Text(stringResource(android.R.string.ok), color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@ComponentPreview
@Composable
private fun LegalDialogPreview() {
    CheckFirmTheme { Surface { LegalDialog(onDismiss = {}) } }
}
