package com.illusion.checkfirm.core.designsystem.component

import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.illusion.checkfirm.core.designsystem.preview.ComponentPreview
import com.illusion.checkfirm.core.designsystem.theme.CheckFirmTheme
import kotlinx.coroutines.launch
import kotlin.math.abs

private val ToolbarHeight = 56.dp

@Composable
fun OneScaffold(
    modifier: Modifier = Modifier,
    title: String = "",
    subTitle: String? = null,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    expandable: Boolean = true,
    content: @Composable ColumnScope.(PaddingValues) -> Unit,
) {
    if (!expandable) {
        OneFixedScaffold(
            modifier = modifier,
            title = title,
            navigationIcon = navigationIcon,
            actions = actions,
            floatingActionButton = floatingActionButton,
            content = content,
        )
        return
    }

    val windowInfo = LocalWindowInfo.current
    val screenHeight = windowInfo.containerDpSize.height
    val expandedHeight = remember(screenHeight) { screenHeight * 0.38f }

    val density = LocalDensity.current
    val limitPx = with(density) { (expandedHeight - ToolbarHeight).toPx() }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        state = rememberTopAppBarState(
            initialHeightOffsetLimit = -limitPx,
            initialHeightOffset = -limitPx,
        ),
        snapAnimationSpec = spring(stiffness = 800f),
        flingAnimationSpec = null,
    )

    val coroutineScope = rememberCoroutineScope()
    val dragModifier = Modifier.draggable(
        orientation = Orientation.Vertical,
        state = rememberDraggableState { delta ->
            scrollBehavior.state.heightOffset += delta
        },
        onDragStopped = { velocity ->
            coroutineScope.launch {
                settleAppBar(scrollBehavior.state, velocity)
            }
        },
    )

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            // Only the navigation/action icons live in the topBar, so they stay drawn
            // on top of the content (content scrolls behind them, never over them).
            OneCollapsingControls(
                modifier = dragModifier,
                navigationIcon = navigationIcon,
                actions = actions,
                expandedHeight = expandedHeight,
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = floatingActionButton,
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            // The title sits behind the content: as content scrolls up it slides over
            // the title, while the icons in the topBar above stay visible.
            OneCollapsingTitle(
                title = title,
                subTitle = subTitle,
                navigationIcon = navigationIcon,
                actions = actions,
                expandedHeight = expandedHeight,
                scrollBehavior = scrollBehavior,
            )

            // No top padding here: content fills the full height behind the (transparent)
            // collapsing toolbar so it can scroll up above/behind it. Callers inset their
            // scrollable content with innerPadding.calculateTopPadding() instead.
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .then(dragModifier),
            ) {
                content(innerPadding)
            }
        }
    }
}

@Composable
private fun OneFixedScaffold(
    modifier: Modifier,
    title: String,
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
            .background(color = MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.statusBars)
            .height(ToolbarHeight)
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        navigationIcon()

        Text(
            text = title,
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp),
            color = CheckFirmTheme.colors.toolbarText,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            style = MaterialTheme.typography.titleMedium,
        )

        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            content = actions,
        )
    }
}

/**
 * The on-top layer of the collapsing toolbar: just the navigation icon and actions.
 *
 * This is placed in the [Scaffold] topBar slot, so it is drawn above the content and
 * reserves the full collapsing height that the Scaffold reports as the top inset. The
 * background is transparent — see [OneCollapsingTitle] for the title that sits behind
 * the content.
 */
@Composable
private fun OneCollapsingControls(
    modifier: Modifier,
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
            .windowInsetsPadding(insets = WindowInsets.statusBars)
            .height(currentHeight)
            .then(other = modifier),
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .defaultMinSize(minHeight = ToolbarHeight)
                .padding(start = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            navigationIcon()

            // Empty middle where the title would be; the title itself is rendered in
            // OneCollapsingTitle behind the content so content can scroll over it.
            Spacer(modifier = Modifier.weight(1f))

            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                content = actions,
            )
        }
    }
}

/**
 * The behind-the-content layer of the collapsing toolbar: the big expanded title and the
 * pinned collapsed title. It is drawn first inside the content area so scrolling content
 * slides over it. The navigation/action icons are re-rendered here as invisible, semantics-
 * cleared placeholders purely so the collapsed title lines up with the real icons that
 * [OneCollapsingControls] draws on top.
 */
@Composable
private fun OneCollapsingTitle(
    title: String,
    subTitle: String?,
    navigationIcon: @Composable () -> Unit,
    actions: @Composable RowScope.() -> Unit,
    expandedHeight: Dp,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val collapsedFraction = scrollBehavior.state.collapsedFraction
    val currentHeight = expandedHeight - (expandedHeight - ToolbarHeight) * collapsedFraction

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(insets = WindowInsets.statusBars)
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
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.displaySmall,
                )
                if (!subTitle.isNullOrBlank()) {
                    Text(
                        text = subTitle,
                        color = CheckFirmTheme.colors.toolbarText,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }

        // Pinned collapsed title, aligned with the real icons via invisible placeholders
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .defaultMinSize(minHeight = ToolbarHeight)
                .padding(start = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .alpha(0f)
                    .clearAndSetSemantics {},
            ) {
                navigationIcon()
            }

            Text(
                text = title,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp)
                    .graphicsLayer {
                        // Fade the title out as content scrolls up over it, so it doesn't
                        // peek through the gaps between items. contentOffset stays 0 while
                        // the bar collapses and only grows once content scrolls past it.
                        val overlap = (-scrollBehavior.state.contentOffset).coerceAtLeast(0f)
                        val notOverlapped =
                            (1f - overlap / (ToolbarHeight.toPx() / 2f)).coerceIn(0f, 1f)
                        alpha = collapsedFraction * notOverlapped
                    },
                color = CheckFirmTheme.colors.toolbarText,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                maxLines = 1,
                style = MaterialTheme.typography.titleLarge,
            )

            Box(
                modifier = Modifier
                    .alpha(0f)
                    .clearAndSetSemantics {},
            ) {
                Row(content = actions)
            }
        }
    }
}

private suspend fun settleAppBar(
    state: androidx.compose.material3.TopAppBarState,
    velocity: Float,
) {
    if (state.collapsedFraction <= 0.01f || state.collapsedFraction >= 0.99f) return

    val target = if (state.collapsedFraction < 0.5f) 0f else state.heightOffsetLimit
    animate(
        initialValue = state.heightOffset,
        targetValue = target,
        initialVelocity = velocity,
        animationSpec = spring(stiffness = 800f),
    ) { value, _ ->
        state.heightOffset = value
    }
    if (abs(velocity) > 0f && target == 0f) Unit
}

@ComponentPreview
@Composable
private fun OneScaffoldPreview() {
    CheckFirmTheme {
        Surface {
            OneScaffold(
                title = "CheckFirm",
            ) { innerPadding ->
                Text(
                    text = "content",
                    modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
                )
            }
        }
    }
}

@ComponentPreview
@Composable
private fun OneScaffoldFixedPreview() {
    CheckFirmTheme {
        Surface {
            OneScaffold(
                title = "CheckFirm",
                expandable = false,
            ) {
                Text(text = "content")
            }
        }
    }
}
