package com.illusion.checkfirm.core.designsystem.component

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

@Composable
fun OneTab(
    titles: List<String>,
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    indicatorColor: Color? = null,
) {
    val darkTheme = isSystemInDarkTheme()
    val containerColor = MaterialTheme.colorScheme.background
    val contentColor = if (darkTheme) Color(0xFFFFFFFF) else Color(0xFF000000)
    val unselectedContentColor = if (darkTheme) Color(0xFFA9A9A9) else Color(0xFF636363)
    val resolvedIndicator = indicatorColor ?: contentColor

    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                color = resolvedIndicator,
                height = 2.dp
            )
        },
        divider = {}
    ) {
        titles.forEachIndexed { index, title ->
            val selected = selectedTabIndex == index
            Tab(
                selected = selected,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                        ),
                        color = if (selected) contentColor else unselectedContentColor
                    )
                }
            )
        }
    }
}

@ComponentPreview
@Composable
private fun OneTabPreview() {
    CheckFirmTheme {
        Surface {
            OneTab(
                titles = listOf("Bookmark", "History"),
                selectedTabIndex = 0,
                onTabSelected = {},
            )
        }
    }
}
