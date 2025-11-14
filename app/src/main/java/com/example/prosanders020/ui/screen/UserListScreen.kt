package com.example.prosanders020.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.prosanders020.data.User
import com.example.prosanders020.ui.viewmodel.UserListViewModel

@Composable
fun UserListScreen(
    viewModel: UserListViewModel = hiltViewModel(),
    onNavigateBackToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 48.dp, start = 16.dp, end = 16.dp)
    ) {
        Text(text = "Daftar Pengguna", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            // 2. Use the user list from the uiState
            items(items = uiState.users, key = { it.nim }) { user ->
                UserCard(
                    user = user,
                    // 3. Connect the click handlers
                    onUpdateClick = { viewModel.onUpdateUserClicked(user) },
                    onDeleteClick = { viewModel.deleteUser(user) }
                )
                Spacer(Modifier.height(8.dp))
            }
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = onNavigateBackToLogin) {
            Text(text = "Kembali ke Login")
        }

        Spacer(Modifier.height(16.dp))
    }

    if (uiState.userBeingEdited != null) {
        EditUserDialog(
            userName = uiState.editDialogName,
            onNameChange = viewModel::onEditDialogNameChanged,
            onConfirm = viewModel::onEditDialogConfirm,
            onDismiss = viewModel::onEditDialogDismiss
        )
    }
}

@Composable
fun UserCard(
    user: User,
    onUpdateClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.weight(1f)) {
                Text(text = "NIM: ${user.nim}", style = MaterialTheme.typography.titleMedium)
                Text(text = "Nama: ${user.nama}", style = MaterialTheme.typography.bodyMedium)
            }

            Button(
                onClick = onUpdateClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFC5CAE9), // V1.0 "biru muda"
                    contentColor = Color(0xFF303F9F)  // V1.0 "teks biru tua"
                )
            ) {
                Text("Update")
            }

            Spacer(Modifier.width(8.dp))

            Button(
                onClick = onDeleteClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFCDD2),
                    contentColor = Color(0xFFB71C1C)
                )
            ) {
                Text("Hapus")
            }
        }
    }
}

@Composable
fun EditUserDialog(
    userName: String,
    onNameChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Update Nama") },
        text = {
            OutlinedTextField(
                value = userName,
                onValueChange = onNameChange,
                label = { Text("Nama Baru") }
            )
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Simpan")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}