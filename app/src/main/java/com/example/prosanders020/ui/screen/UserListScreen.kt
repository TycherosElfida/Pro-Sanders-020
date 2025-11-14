package com.example.prosanders020.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
    // 1. Collect the reactive list of users
    val users by viewModel.users.collectAsStateWithLifecycle()

    // 2. The V1.0 'LaunchedEffect(Unit)' to call 'loadUsers()' is GONE.
    //    It is 100% unnecessary.

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 48.dp, start = 16.dp, end = 16.dp)
    ) {
        Text(text = "Daftar Pengguna", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(items = users, key = { it.nim }) { user ->
                UserCard(
                    user = user,
                    onDeleteClick = { viewModel.deleteUser(user) }
                )
                Spacer(Modifier.height(8.dp))
            }
        }

        Spacer(Modifier.height(16.dp))

        // This replaces the V1.0 "Kembali ke Register" button
        Button(onClick = onNavigateBackToLogin) {
            Text(text = "Kembali ke Login")
        }

        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun UserCard(
    user: User,
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

            // "Update" button is not implemented as per V1.0 logic

            Button(
                onClick = onDeleteClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFCDD2), // V1.0 "merah muda"
                    contentColor = Color(0xFFB71C1C) // V1.0 "teks merah tua"
                )
            ) {
                Text("Hapus")
            }
        }
    }
}