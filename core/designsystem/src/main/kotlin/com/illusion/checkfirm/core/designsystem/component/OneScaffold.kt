package com.illusion.checkfirm.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme

private val ToolbarHeight = 56.dp

@Composable
fun OneScaffold(
    title: String,
    modifier: Modifier = Modifier,
    subTitle: String? = null,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    expandable: Boolean = true,
    content: @Composable ColumnScope.(PaddingValues) -> Unit,
) {
    if (!expandable) {
        OneFixedScaffold(
            title = title,
            modifier = modifier,
            navigationIcon = navigationIcon,
            actions = actions,
            floatingActionButton = floatingActionButton,
            content = content,
        )
        return
    }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val expandedHeight = remember(screenHeight) { screenHeight * 0.38f }

    val density = LocalDensity.current
    val limitPx = with(density) { (expandedHeight - ToolbarHeight).toPx() }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState(
            initialHeightOffsetLimit = -limitPx,
            initialHeightOffset = -limitPx,
        ),
    )

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            OneCollapsingToolbar(
                title = title,
                subTitle = subTitle,
                navigationIcon = navigationIcon,
                actions = actions,
                expandedHeight = expandedHeight,
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = floatingActionButton,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
        ) {
            content(innerPadding)
        }
    }
}

@Composable
private fun OneFixedScaffold(
    title: String,
    modifier: Modifier,
    navigationIcon: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit,
    floatingActionButton: @Composable () -> Unit,
    content: @Composable ColumnScope.(PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            OneFixedToolbar(
                title = title,
                navigationIcon = navigationIcon,
                actions = actions,
            )
        },
        floatingActionButton = floatingActionButton,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
        ) {
            content(innerPadding)
        }
    }
}

@Composable
private fun OneFixedToolbar(
    title: String,
    navigationIcon: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.statusBars)
            .height(ToolbarHeight)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        navigationIcon()

        Text(
            text = title,
            color = CheckFirmTheme.colors.toolbarText,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp),
        )

        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            content = actions,
        )
    }
}

@Composable
private fun OneCollapsingToolbar(
    title: String,
    subTitle: String?,
    navigationIcon: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit,
    expandedHeight: Dp,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val collapsedFraction = scrollBehavior.state.collapsedFraction
    val currentHeight = expandedHeight - (expandedHeight - ToolbarHeight) * collapsedFraction

    val density = LocalDensity.current
    val expandedPx = with(density) { expandedHeight.toPx() }
    val collapsedPx = with(density) { ToolbarHeight.toPx() }
    SideEffect {
        scrollBehavior.state.heightOffsetLimit = -(expandedPx - collapsedPx)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.statusBars)
            .height(currentHeight),
    ) {
        // Expanded centered big title
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = 1f - collapsedFraction },
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = title,
                    color = CheckFirmTheme.colors.toolbarText,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                )
                if (!subTitle.isNullOrBlank()) {
                    Text(
                        text = subTitle,
                        color = CheckFirmTheme.colors.toolbarText,
                        fontSize = 14.sp,
                    )
                }
            }
        }

        // Pinned bottom 56dp toolbar
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .height(ToolbarHeight)
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            navigationIcon()

            Text(
                text = title,
                color = CheckFirmTheme.colors.toolbarText,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp)
                    .graphicsLayer { alpha = collapsedFraction },
            )

            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                content = actions,
            )
        }
    }
}

@ComponentPreview
@Composable
private fun OneScaffoldPreview() {
    CheckFirmTheme {
        OneScaffold(title = "CheckFirm") { }
    }
}

@ComponentPreview
@Composable
private fun OneScaffoldFixedPreview() {
    CheckFirmTheme {
        OneScaffold(title = "CheckFirm", expandable = false) { }
    }
}
