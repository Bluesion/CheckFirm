package com.illusion.checkfirm.feature.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneCard
import com.illusion.checkfirm.core.designsystem.component.OneCardItem
import com.illusion.checkfirm.core.designsystem.component.OneDivider
import com.illusion.checkfirm.core.designsystem.component.OneIcons
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.feature.home.R as FeatureR

@Composable
internal fun HelloDashboard(
    onSearchClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onWelcomeSearchClick: () -> Unit,
    onInfoCatcherClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = OneIcons.CheckFirm,
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(64.dp),
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(FeatureR.string.home_hello_title),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = stringResource(FeatureR.string.home_hello_description),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(24.dp))

        OneCard {
            OneCardItem(
                title = stringResource(R.string.search),
                description = stringResource(FeatureR.string.home_hello_search_description),
                iconVector = OneIcons.Search,
                onClick = onSearchClick,
            )
            OneDivider()
            OneCardItem(
                title = stringResource(R.string.bookmark),
                description = stringResource(FeatureR.string.home_hello_bookmark_description),
                iconVector = OneIcons.Bookmark,
                onClick = onBookmarkClick,
            )
            OneDivider()
            OneCardItem(
                title = stringResource(R.string.welcome_search),
                description = stringResource(R.string.settings_welcome_search_description),
                iconVector = OneIcons.WelcomeSearch,
                onClick = onWelcomeSearchClick,
            )
            OneDivider()
            OneCardItem(
                title = stringResource(R.string.info_catcher),
                description = stringResource(FeatureR.string.home_hello_info_catcher_description),
                iconVector = OneIcons.InfoCatcher,
                onClick = onInfoCatcherClick,
            )
            OneDivider()
            OneCardItem(
                title = stringResource(R.string.settings),
                description = stringResource(FeatureR.string.home_hello_settings_description),
                iconVector = OneIcons.Settings,
                onClick = onSettingsClick,
            )
        }
    }
}

@ComponentPreview
@Composable
fun HelloDashboardPreview() {
    CheckFirmTheme {
        Surface {
            HelloDashboard(
                onSearchClick = {},
                onBookmarkClick = {},
                onWelcomeSearchClick = {},
                onInfoCatcherClick = {},
                onSettingsClick = {},
            )
        }
    }
}
