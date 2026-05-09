package com.illusion.checkfirm.core.designsystem.theme

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import dev.chrisbanes.haze.HazeState

/**
 * Optional [HazeState] used by design-system surfaces (e.g. [com.illusion.checkfirm.core.designsystem.component.OneAlertDialog])
 * that want to render a frosted-glass effect over the activity content.
 *
 * Wire-up at the app/activity root:
 * ```
 * val hazeState = remember { HazeState() }
 * CompositionLocalProvider(LocalHazeState provides hazeState) {
 *     Box(modifier = Modifier.hazeSource(hazeState)) { /* app content */ }
 * }
 * ```
 *
 * If left null (default), the dialog falls back to an opaque background.
 */
val LocalHazeState: ProvidableCompositionLocal<HazeState?> = compositionLocalOf { null }
