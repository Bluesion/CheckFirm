package com.illusion.checkfirm.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.layout.*
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
    textStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium,
    unselectedColor: Color = CheckFirmTheme.colors.tabUnselected,
    selectedFontWeight: FontWeight = FontWeight.Bold,
) {
    val containerColor = MaterialTheme.colorScheme.background
    val contentColor = MaterialTheme.colorScheme.onSurface
    val unselectedContentColor = unselectedColor
    val resolvedIndicator = indicatorColor ?: contentColor

    androidx.compose.foundation.layout.Row(modifier.then(Modifier.fillMaxWidth())) {
        titles.forEachIndexed { index, title ->
            val selected = selectedTabIndex == index
            androidx.compose.foundation.layout.Box(
                Modifier.weight(1f).selectable(selected, role = androidx.compose.ui.semantics.Role.Tab) { onTabSelected(index) }.padding(vertical = 12.dp),
                contentAlignment = androidx.compose.ui.Alignment.Center,
            ) {
                androidx.compose.foundation.layout.Column(Modifier.width(androidx.compose.foundation.layout.IntrinsicSize.Min), horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                    Text(title, style = textStyle, textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        fontWeight = if (selected) selectedFontWeight else FontWeight.Normal,
                        color = if (selected) resolvedIndicator else unselectedContentColor)
                    androidx.compose.foundation.layout.Spacer(Modifier.height(2.dp))
                    androidx.compose.foundation.layout.Box(Modifier.fillMaxWidth().height(2.dp).background(if (selected) resolvedIndicator else Color.Transparent))
                }
            }
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
