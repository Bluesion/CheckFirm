package com.illusion.checkfirm.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Devices.PIXEL_2
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import com.illusion.checkfirm.core.designsystem.component.OneUIDivider
import com.illusion.checkfirm.core.designsystem.component.OneUISwitch

@Composable
fun ProfileCard(
    profileName: String = "Unknown",
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = profileName,
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(modifier = Modifier.size(12.dp))
            Surface(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape,
            ) {
                Icon(
                    modifier = Modifier.padding(8.dp),
                    imageVector = PreferenceIcon.Profile,
                    contentDescription = null,
                )
            }
        }
    }
}

@Composable
fun AppearanceCard(
    onThemeClick: () -> Unit,
    onLanguageClick: () -> Unit,
    isQuickSearchBarEnabled: Boolean,
    onQuickSearchBarClick: () -> Unit,
    onQuickSearchBarChanged: (Boolean) -> Unit,
) {
    PreferenceCard {
        PreferenceItem(
            onClick = onThemeClick,
            title = stringResource(R.string.settings_theme),
            description = stringResource(R.string.settings_theme_description),
        )
        OneUIDivider(modifier = Modifier.padding(horizontal = 12.dp))
        PreferenceItem(
            onClick = onLanguageClick,
            title = stringResource(R.string.settings_language),
            description = stringResource(R.string.settings_language_description),
        )
        OneUIDivider(modifier = Modifier.padding(horizontal = 12.dp))
        PreferenceItem(
            onClick = onQuickSearchBarClick,
            title = stringResource(R.string.settings_quick_search_bar),
            description = stringResource(R.string.settings_quick_search_bar_description),
            isSwitchChecked = isQuickSearchBarEnabled,
            onSwitchToggle = onQuickSearchBarChanged,
        )
    }
}

@Composable
fun BookmarkCard(
    onBookmarkOrderClick: () -> Unit,
    onBookmarkResetClick: () -> Unit,
    onBackupRestoreClick: () -> Unit,
) {
    PreferenceCard {
        PreferenceItem(
            onClick = onBookmarkOrderClick,
            title = stringResource(R.string.settings_bookmark_order),
            description = stringResource(R.string.settings_bookmark_order_description),
        )
        OneUIDivider(modifier = Modifier.padding(horizontal = 12.dp))
        PreferenceItem(
            onClick = onBookmarkResetClick,
            title = stringResource(R.string.settings_bookmark_reset),
            description = stringResource(R.string.settings_bookmark_reset_description),
        )
        OneUIDivider(modifier = Modifier.padding(horizontal = 12.dp))
        PreferenceItem(
            onClick = onBackupRestoreClick,
            title = stringResource(R.string.settings_bookmark_backup_restore),
            description = stringResource(R.string.settings_bookmark_backup_restore_description),
        )
    }
}

@Composable
fun SearchCard(
    isWelcomeSearchEnabled: Boolean,
    onWelcomeSearchClick: () -> Unit,
    onWelcomeSearchChanged: (Boolean) -> Unit,
    isInfoCatcherEnabled: Boolean,
    onInfoCatcherClick: () -> Unit,
    onInfoCatcherChanged: (Boolean) -> Unit,
    isFirebaseEnabled: Boolean,
    onFirebaseClick: () -> Unit,
    onFirebaseChanged: (Boolean) -> Unit,
) {
    PreferenceCard {
        PreferenceItem(
            onClick = onWelcomeSearchClick,
            title = stringResource(R.string.welcome_search),
            description = stringResource(R.string.settings_welcome_search_description),
            isSwitchChecked = isWelcomeSearchEnabled,
            onSwitchToggle = onWelcomeSearchChanged,
        )
        OneUIDivider(modifier = Modifier.padding(horizontal = 12.dp))
        PreferenceItem(
            onClick = onInfoCatcherClick,
            title = stringResource(R.string.info_catcher),
            description = stringResource(R.string.settings_info_catcher_description),
            isSwitchChecked = isInfoCatcherEnabled,
            onSwitchToggle = onInfoCatcherChanged,
        )
        OneUIDivider(modifier = Modifier.padding(horizontal = 12.dp))
        PreferenceItem(
            onClick = onFirebaseClick,
            title = stringResource(R.string.settings_firebase),
            description = stringResource(R.string.settings_firebase_description),
            isSwitchChecked = isFirebaseEnabled,
            onSwitchToggle = onFirebaseChanged,
        )
    }
}

@Composable
fun AboutCard(
    onHelpClick: () -> Unit,
    onAboutClick: () -> Unit,
    onInquiryClick: () -> Unit,
) {
    PreferenceCard {
        PreferenceItem(
            onClick = onHelpClick,
            title = stringResource(R.string.help),
        )
        OneUIDivider(modifier = Modifier.padding(horizontal = 12.dp))
        PreferenceItem(
            onClick = onAboutClick,
            title = stringResource(R.string.settings_about),
        )
        OneUIDivider(modifier = Modifier.padding(horizontal = 12.dp))
        PreferenceItem(
            onClick = onInquiryClick,
            title = stringResource(R.string.settings_inquiry),
        )
    }
}

@Composable
private fun PreferenceCard(
    content: @Composable ColumnScope. () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            content()
        }
    }
}

@Composable
private fun PreferenceItem(
    onClick: () -> Unit,
    title: String,
    description: String? = null,
    isSwitchChecked: Boolean = false,
    onSwitchToggle: ((Boolean) -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(12.dp)
            .defaultMinSize(minHeight = 48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
            )
            if (description != null) {
                Text(
                    text = description,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        if (onSwitchToggle != null) {
            Spacer(modifier = Modifier.size(12.dp))
            OneUISwitch(
                checked = isSwitchChecked,
                onCheckedChange = onSwitchToggle
            )
        }
    }
}

@Preview(showBackground = true, device = PIXEL_2)
@Preview(showBackground = true, device = PIXEL_2, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreferenceCardPreview() {
    CheckFirmTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            ProfileCard(
                profileName = "TEST PROFILE",
                onClick = {},
            )
            AppearanceCard(
                onThemeClick = {},
                onLanguageClick = {},
                isQuickSearchBarEnabled = false,
                onQuickSearchBarClick = {},
                onQuickSearchBarChanged = {},
            )
            BookmarkCard(
                onBookmarkOrderClick = {},
                onBookmarkResetClick = {},
                onBackupRestoreClick = {},
            )
            SearchCard(
                isWelcomeSearchEnabled = false,
                onWelcomeSearchClick = {},
                onWelcomeSearchChanged = {},
                isInfoCatcherEnabled = false,
                onInfoCatcherClick = {},
                onInfoCatcherChanged = {},
                isFirebaseEnabled = false,
                onFirebaseClick = {},
                onFirebaseChanged = {},
            )
            AboutCard(
                onHelpClick = {},
                onAboutClick = {},
                onInquiryClick = {}
            )
        }
    }
}