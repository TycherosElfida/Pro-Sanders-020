package com.example.prosanders020.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prosanders020.data.User
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

data class RegisterUiState(
    val nim: String = "",
    val nama: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val nimError: String? = null,
    val namaError: String? = null,
    val passwordError: String? = null
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    // --- Event Handlers ---

    fun onNimChanged(nim: String) {
        _uiState.update { it.copy(nim = nim, nimError = null) }
    }

    fun onNamaChanged(nama: String) {
        _uiState.update { it.copy(nama = nama, namaError = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null) }
    }

    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun onRegisterClicked() {
        val state = _uiState.value

        val nimError = if (state.nim.isBlank()) "NIM tidak boleh kosong" else null
        val namaError = if (state.nama.isBlank()) "Nama tidak boleh kosong" else null
        val passwordError = if (state.password.isBlank()) "Password tidak boleh kosong" else null

        if (nimError != null || namaError != null || passwordError != null) {
            _uiState.update {
                it.copy(
                    nimError = nimError,
                    namaError = namaError,
                    passwordError = passwordError
                )
            }
            return
        }

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                val hashedPass = BCrypt.hashpw(state.password, BCrypt.gensalt())

                val secureUser = User(
                    nim = state.nim,
                    nama = state.nama,
                    passwordHash = hashedPass
                )

                repository.register(secureUser)

                _eventFlow.emit(UiEvent.ShowToast("Data berhasil disimpan"))
                _eventFlow.emit(UiEvent.NavigationSuccess)

            } catch (e: Exception) {
                _eventFlow.emit(UiEvent.ShowToast("Gagal menyimpan data: ${e.message}"))
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}