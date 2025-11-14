package com.example.prosanders020.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prosanders020.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt
import javax.inject.Inject

data class LoginUiState(
    val nim: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val passwordVisible: Boolean = false,
    val rememberMe: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onNimChanged(nim: String) {
        _uiState.update { it.copy(nim = nim, error = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, error = null) }
    }

    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun onRememberMeChanged(isChecked: Boolean) {
        _uiState.update { it.copy(rememberMe = isChecked) }
        // TODO: Add logic here to save token to DataStore if isChecked is true
    }
    fun onLoginClicked() {
        val state = _uiState.value

        if (state.nim.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(error = "NIM and Password cannot be blank") }
            return
        }

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                val user = repository.getUserWithNim(state.nim)

                if (user != null && BCrypt.checkpw(state.password, user.passwordHash)) {
                    _eventFlow.emit(UiEvent.NavigationSuccess)
                } else {
                    _eventFlow.emit(UiEvent.ShowToast("Invalid NIM or Password"))
                }
            } catch (e: Exception) {
                _eventFlow.emit(UiEvent.ShowToast("Error: ${e.message}"))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}