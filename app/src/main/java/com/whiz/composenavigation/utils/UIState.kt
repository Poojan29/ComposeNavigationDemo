package com.whiz.composenavigation.utils

sealed class UIState {
    object SignedOut : UIState()
    object InProgress : UIState()
    object Error : UIState()
    object SignIn : UIState()
    object PassCodeConfirm : UIState()
    object PassCodeError : UIState()
    object PassCodeNotEntered : UIState()
}