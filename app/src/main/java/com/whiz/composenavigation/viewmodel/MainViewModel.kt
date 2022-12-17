package com.whiz.composenavigation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whiz.composenavigation.utils.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _activeStep = MutableStateFlow(Step.Create)
    private val _filledDots = MutableStateFlow(0)

    private var createPasscode: StringBuilder = StringBuilder()
    private var confirmPasscode: StringBuilder = StringBuilder()

    val activeStep = _activeStep.asStateFlow()
    val filledDots = _filledDots.asStateFlow()

    private var _uiState = mutableStateOf<UIState>(UIState.PassCodeNotEntered)
    val uiState: State<UIState>
        get() = _uiState

    private fun emitActiveStep(activeStep: Step) = viewModelScope.launch {
        _activeStep.emit(activeStep)
    }

    private fun emitFilledDots(filledDots: Int) = viewModelScope.launch {
        _filledDots.emit(filledDots)
    }

    private fun resetData() {
        _uiState = mutableStateOf(UIState.PassCodeNotEntered)
        emitActiveStep(Step.Create)
        emitFilledDots(0)

        createPasscode.clear()
        confirmPasscode.clear()
    }

    fun enterKey(key: String) {
        if (_filledDots.value >= PASSCODE_LENGTH) {
            return
        }

        emitFilledDots(
            if (_activeStep.value == Step.Create) {
                createPasscode.append(key)

                createPasscode.length
            } else {
                confirmPasscode.append(key)

                confirmPasscode.length
            }
        )

        if (_filledDots.value == PASSCODE_LENGTH) {
            if (_activeStep.value == Step.Create) {
                emitActiveStep(Step.Confirm)
                emitFilledDots(0)
            } else {
                _uiState = if (createPasscode.toString() == confirmPasscode.toString()) {
                    resetData()
                    mutableStateOf(UIState.PassCodeConfirm)
                } else {
                    mutableStateOf(UIState.PassCodeError)
                }
            }
        }
    }

    fun deleteKey() {
        _filledDots.tryEmit(
            if (_activeStep.value == Step.Create) {
                if (createPasscode.isNotEmpty()) {
                    createPasscode.deleteAt(createPasscode.length - 1)
                }

                createPasscode.length
            } else {
                if (confirmPasscode.isNotEmpty()) {
                    confirmPasscode.deleteAt(confirmPasscode.length - 1)
                }

                confirmPasscode.length
            }
        )
    }

    fun deleteAllKeys() {
        if (_activeStep.value == Step.Create) {
            createPasscode.clear()
        } else {
            confirmPasscode.clear()
        }

        emitFilledDots(0)
    }

    fun restart() = resetData()

    enum class Step(var index: Int) {
        Create(0),
        Confirm(1)
    }

    companion object {

        const val STEPS_COUNT = 2
        const val PASSCODE_LENGTH = 4
    }
}