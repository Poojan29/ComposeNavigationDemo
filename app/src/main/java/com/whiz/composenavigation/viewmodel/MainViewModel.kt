package com.whiz.composenavigation.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.whiz.composenavigation.utils.Constant
import com.whiz.composenavigation.utils.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    context: Context,
) : ViewModel() {

    val pref = context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE)

    private var isPassSet: Boolean = pref.getBoolean(Constant.IS_PASSCODE_SET, false)
    var passcode = pref.getString(Constant.PASSCODE, "")

    init {
        Log.d("YES", isPassSet.toString())
        Log.d("Yes", passcode.toString())
    }

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
                if (!isPassSet) {
                    emitActiveStep(Step.Confirm)
                    emitFilledDots(0)
                } else {
                    if (createPasscode.toString() == passcode) {
                        _uiState = mutableStateOf(UIState.PassCodeConfirm)
                    } else {
                        _uiState = mutableStateOf(UIState.PassCodeError)
                    }
                }
            } else {
                _uiState = if (createPasscode.toString() == confirmPasscode.toString()) {
                    pref.edit().putString(Constant.PASSCODE, confirmPasscode.toString()).apply()
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

    enum class Step {
        Create,
        Confirm
    }

    companion object {
        const val PASSCODE_LENGTH = 4
    }
}