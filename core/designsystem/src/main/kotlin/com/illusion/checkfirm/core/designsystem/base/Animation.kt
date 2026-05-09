package com.illusion.checkfirm.core.designsystem.base

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.launch

fun Modifier.bounceClick(
    pressScale: Float = 0.99f,
    onClick: () -> Unit,
) = composed {
    val scale = remember { Animatable(initialValue = 1f) }
    val scope = rememberCoroutineScope()
    val interactionSource = remember { MutableInteractionSource() }
    // pointerInput(Unit) 블록은 한 번만 셋업되므로 onClick 람다를 그대로 캡처하면
    // 재구성 시 새로 들어온 람다(최신 state를 캡처한)가 반영되지 않는다.
    // rememberUpdatedState로 최신 onClick을 가리키게 한다.
    val currentOnClick by rememberUpdatedState(onClick)

    this
        // indication은 scale 밖에 둬서 리플은 원래 크기로 그려지도록
        .indication(
            interactionSource = interactionSource,
            indication = LocalIndication.current,
        )
        // drawWithContent 안의 내용물(자식)만 pressScale로 축소
        .drawWithContent {
            scale(scale.value) {
                this@drawWithContent.drawContent()
            }
        }
        .pointerInput(Unit) {
            detectTapGestures(
                onPress = { offset ->
                    val press = PressInteraction.Press(pressPosition = offset)
                    interactionSource.emit(interaction = press)

                    scope.launch {
                        scale.animateTo(
                            targetValue = pressScale,
                            animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                        )
                    }

                    val released = tryAwaitRelease()

                    if (released) {
                        interactionSource.emit(interaction = PressInteraction.Release(press))
                    } else {
                        interactionSource.emit(interaction = PressInteraction.Cancel(press))
                    }

                    scope.launch {
                        scale.animateTo(
                            targetValue = 1f,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessMediumLow,
                            ),
                        )
                    }
                },
                onTap = { currentOnClick() }
            )
        }
}
