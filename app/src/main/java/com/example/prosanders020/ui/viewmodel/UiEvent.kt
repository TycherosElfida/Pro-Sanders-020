package com.example.prosanders020.ui.viewmodel

sealed interface UiEvent {
    data class ShowToast(val message: String) : UiEvent
    object NavigationSuccess : UiEvent
}