package com.whiz.composenavigation.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.whiz.composenavigation.R
import com.whiz.composenavigation.ui.theme.PasscodeKeyButtonStyle
import com.whiz.composenavigation.utils.UIState
import com.whiz.composenavigation.viewmodel.MainViewModel

@Composable
fun FirstScreen(
    popToSecondScreen: () -> Unit,
    viewModel: MainViewModel
) {
    val activeStep by viewModel.activeStep.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.0F to Color.Transparent,
                        1.0F to MaterialTheme.colors.onBackground.copy(alpha = 0.045F)
                    )
                )
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Toolbar(activeStep = activeStep)
        Spacer(modifier = Modifier.height(6.dp))
        Headers(activeStep = activeStep)
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier.weight(1.0F),
            contentAlignment = Alignment.Center
        ) {
            PasscodeView(viewModel = viewModel, popToSecondScreen = popToSecondScreen)
        }
        Spacer(modifier = Modifier.height(6.dp))
        PasscodeKeys(
            viewModel = viewModel,
            modifier = Modifier.padding(horizontal = 12.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Divider(
            color = MaterialTheme.colors.onBackground.copy(alpha = 0.08F),
            thickness = 1.dp
        )
    }

    SnackbarHost(hostState = snackBarHostState)
}

@Composable
private fun Toolbar(activeStep: MainViewModel.Step) {

    Box(modifier = Modifier.fillMaxWidth()) {
        StepIndicator(
            modifier = Modifier.align(alignment = Alignment.Center),
            activeStep = activeStep
        )
    }
}

@Composable
fun StepIndicator(
    modifier: Modifier = Modifier,
    activeStep: MainViewModel.Step
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            space = 6.dp,
            alignment = Alignment.CenterHorizontally
        )
    ) {
        repeat(MainViewModel.STEPS_COUNT) { step ->
            val isActiveStep = step <= activeStep.index
            val stepColor = animateColorAsState(
                if (isActiveStep) {
                    MaterialTheme.colors.primary
                } else {
                    MaterialTheme.colors.secondary
                }
            )

            Box(
                modifier = Modifier
                    .size(
                        width = 72.dp,
                        height = 4.dp
                    )
                    .background(
                        color = stepColor.value,
                        shape = MaterialTheme.shapes.medium
                    )
            )
        }
    }
}

@Composable
fun Headers(
    modifier: Modifier = Modifier,
    activeStep: MainViewModel.Step
) {
    val transitionState = remember { MutableTransitionState(activeStep) }
    transitionState.targetState = activeStep

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        if (activeStep == MainViewModel.Step.Create) {
            Text(
                text = "Create passcode",
                style = MaterialTheme.typography.h6
            )
        } else {
            Text(
                text = "Confirm passcode",
                style = MaterialTheme.typography.h6
            )
        }

    }
}

@Composable
private fun PasscodeView(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    popToSecondScreen: () -> Unit
) {
    val filledDots by viewModel.filledDots.collectAsState()
    val passcodeRejectedDialogVisible = remember {
        mutableStateOf(false)
    }

    if (viewModel.uiState.value == UIState.PassCodeError) {
        passcodeRejectedDialogVisible.value = true
    } else if (viewModel.uiState.value == UIState.PassCodeConfirm) {
        popToSecondScreen.invoke()
    }

    PasscodeRejectedDialog(
        visible = passcodeRejectedDialogVisible.value,
        onDismiss = {
            passcodeRejectedDialogVisible.value = false
            viewModel.restart()
        }
    )

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                space = 26.dp,
                alignment = Alignment.CenterHorizontally
            )
        ) {
            repeat(MainViewModel.PASSCODE_LENGTH) { dot ->
                val isFilledDot = dot + 1 <= filledDots
                val dotColor = animateColorAsState(
                    if (isFilledDot) {
                        MaterialTheme.colors.primary
                    } else {
                        MaterialTheme.colors.secondary
                    }
                )

                Box(
                    modifier = modifier
                        .size(size = 14.dp)
                        .background(
                            color = dotColor.value,
                            shape = CircleShape
                        )
                )
            }
        }
    }
}

@Composable
fun PasscodeRejectedDialog(
    visible: Boolean,
    onDismiss: () -> Unit
) {
    if (visible) {
        AlertDialog(
            shape = MaterialTheme.shapes.small,
            title = { Text(text = "Passcodes do not match!") },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text(text = "Try again")
                }
            },
            onDismissRequest = onDismiss
        )
    }
}

@Composable
private fun PasscodeKeys(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
) {
    val onEnterKeyClick = { keyTitle: String ->
        viewModel.enterKey(keyTitle)
    }

    Box(modifier = modifier) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth()) {
                PasscodeKey(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyTitle = "1",
                    onClick = onEnterKeyClick
                )
                PasscodeKey(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyTitle = "2",
                    onClick = onEnterKeyClick
                )
                PasscodeKey(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyTitle = "3",
                    onClick = onEnterKeyClick
                )
            }
            Spacer(modifier = Modifier.height(height = 12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                PasscodeKey(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyTitle = "4",
                    onClick = onEnterKeyClick
                )
                PasscodeKey(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyTitle = "5",
                    onClick = onEnterKeyClick
                )
                PasscodeKey(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyTitle = "6",
                    onClick = onEnterKeyClick
                )
            }
            Spacer(modifier = Modifier.height(height = 12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                PasscodeKey(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyTitle = "7",
                    onClick = onEnterKeyClick
                )
                PasscodeKey(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyTitle = "8",
                    onClick = onEnterKeyClick
                )
                PasscodeKey(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyTitle = "9",
                    onClick = onEnterKeyClick
                )
            }
            Spacer(modifier = Modifier.height(height = 12.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                PasscodeKey(modifier = Modifier.weight(weight = 1.0F))
                PasscodeKey(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyTitle = "0",
                    onClick = onEnterKeyClick
                )
                PasscodeKey(
                    modifier = Modifier.weight(weight = 1.0F),
                    keyIcon = ImageVector.vectorResource(id = R.drawable.ic_delete),
                    keyIconContentDescription = "Delete Passcode Key Button",
                    onClick = {
                        viewModel.deleteKey()
                    },
                    onLongClick = {
                        viewModel.deleteAllKeys()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CombinedClickableIconButton(
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    rippleRadius: Dp = 36.dp,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .size(size = size)
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
                enabled = enabled,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = rememberRipple(
                    bounded = false,
                    radius = rippleRadius,
                    color = if (isSystemInDarkTheme()) {
                        MaterialTheme.colors.onBackground
                    } else {
                        MaterialTheme.colors.primary
                    }
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        val contentAlpha = if (enabled) LocalContentAlpha.current else ContentAlpha.disabled
        CompositionLocalProvider(LocalContentAlpha provides contentAlpha, content = content)
    }
}

@Composable
private fun PasscodeKey(
    modifier: Modifier = Modifier,
    keyTitle: String = "",
    keyIcon: ImageVector? = null,
    keyIconContentDescription: String = "",
    onClick: ((String) -> Unit)? = null,
    onLongClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CombinedClickableIconButton(
            modifier = Modifier.padding(all = 4.dp),
            onClick = {
                onClick?.invoke(keyTitle)
            },
            onLongClick = {
                onLongClick?.invoke()
            }
        ) {
            if (keyIcon == null) {
                Text(
                    text = keyTitle,
                    style = PasscodeKeyButtonStyle
                )
            } else {
                Icon(
                    imageVector = keyIcon,
                    contentDescription = keyIconContentDescription
                )
            }
        }
    }
}