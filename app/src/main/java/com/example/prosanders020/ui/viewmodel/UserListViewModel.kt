package com.example.prosanders020.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prosanders020.data.User
import com.example.prosanders020.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    // 1. THIS IS THE V2.0 REACTIVE DATA FLOW
    // It converts the "cold" Flow from Room into a "hot" StateFlow
    // that the UI can collect. It's lifecycle-aware.
    val users: StateFlow<List<User>> = repository.getAllUsers()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L), // Keep active 5s
            initialValue = emptyList()
        )

    // 2. The V1.0 'loadUsers()' function is DELETED.
    // It is no longer needed. The Flow handles everything.

    // 3. Deleting a user will automatically trigger the Flow to
    //    emit a new list, and the UI will update.
    fun deleteUser(user: User) {
        viewModelScope.launch {
            repository.deleteUser(user)
        }
    }
}