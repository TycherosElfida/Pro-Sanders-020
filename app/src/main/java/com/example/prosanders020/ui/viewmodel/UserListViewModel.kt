package com.example.prosanders020.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prosanders020.data.User
import com.example.prosanders020.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserListUiState(
    val users: List<User> = emptyList(),
    val userBeingEdited: User? = null,
    val editDialogName: String = ""
)

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _userBeingEdited = MutableStateFlow<User?>(null)
    private val _editDialogName = MutableStateFlow("")
    private val _users = repository.getAllUsers()

    val uiState: StateFlow<UserListUiState> = combine(
        _users,
        _userBeingEdited,
        _editDialogName
    ) { users, userBeingEdited, editDialogName ->
        UserListUiState(
            users = users,
            userBeingEdited = userBeingEdited,
            editDialogName = editDialogName
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = UserListUiState()
    )

    fun deleteUser(user: User) {
        viewModelScope.launch {
            repository.deleteUser(user)
        }
    }

    fun onUpdateUserClicked(user: User) {
        // Open the dialog and pre-fill the text field
        _userBeingEdited.value = user
        _editDialogName.value = user.nama
    }

    fun onEditDialogNameChanged(newName: String) {
        _editDialogName.value = newName
    }

    fun onEditDialogConfirm() {
        val userToUpdate = _userBeingEdited.value ?: return
        val newName = _editDialogName.value

        if (newName.isBlank()) {
            // Optional: Send a toast event for error
            return
        }

        viewModelScope.launch {
            repository.updateUser(
                userToUpdate.copy(nama = newName)
            )
            onEditDialogDismiss() // Close dialog on success
        }
    }

    fun onEditDialogDismiss() {
        _userBeingEdited.value = null
        _editDialogName.value = ""
    }
}