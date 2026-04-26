package com.illusion.checkfirm.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.illusion.checkfirm.core.designsystem.R
import com.illusion.checkfirm.core.designsystem.component.OneIcons
import com.illusion.checkfirm.core.designsystem.component.OneSettingsDivider
import com.illusion.checkfirm.core.designsystem.component.OneSwitch
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

@Composable
fun ProfileCard(
    profileName: String = "Unknown",
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp),
            ) {
                Text(
                    text = stringResource(R.string.settings_profile_user_name),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = profileName,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
            Icon(
                imageVector = OneIcons.IcProfile,
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(50.dp),
            )
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
        OneSettingsDivider()
        PreferenceItem(
            onClick = onLanguageClick,
            title = stringResource(R.string.settings_language),
            description = stringResource(R.string.settings_language_description),
        )
        OneSettingsDivider()
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
        OneSettingsDivider()
        PreferenceItem(
            onClick = onBookmarkResetClick,
            title = stringResource(R.string.settings_bookmark_reset),
            description = stringResource(R.string.settings_bookmark_reset_description),
        )
        OneSettingsDivider()
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
        OneSettingsDivider()
        PreferenceItem(
            onClick = onInfoCatcherClick,
            title = stringResource(R.string.info_catcher),
            description = stringResource(R.string.settings_info_catcher_description),
            isSwitchChecked = isInfoCatcherEnabled,
            onSwitchToggle = onInfoCatcherChanged,
        )
        OneSettingsDivider()
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
        PreferenceItem(onClick = onHelpClick, title = stringResource(R.string.help))
        OneSettingsDivider()
        PreferenceItem(onClick = onAboutClick, title = stringResource(R.string.settings_about))
        OneSettingsDivider()
        PreferenceItem(onClick = onInquiryClick, title = stringResource(R.string.settings_inquiry))
    }
}

@Composable
private fun PreferenceCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) { content() }
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
            .padding(14.dp)
            .defaultMinSize(minHeight = 48.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 17.sp),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
            )
            if (description != null) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp),
                    color = CheckFirmTheme.colors.settingsDescription,
                )
            }
        }
        if (onSwitchToggle != null) {
            Spacer(modifier = Modifier.size(12.dp))
            OneSwitch(
                checked = isSwitchChecked,
                onCheckedChange = onSwitchToggle,
            )
        }
    }
}
