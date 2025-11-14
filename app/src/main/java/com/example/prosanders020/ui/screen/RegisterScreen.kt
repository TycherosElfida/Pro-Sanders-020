package com.example.prosanders020.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.prosanders020.ui.viewmodel.RegisterViewModel
import com.example.prosanders020.ui.viewmodel.UiEvent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onRegisterSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is UiEvent.NavigationSuccess -> {
                    onRegisterSuccess()
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 48.dp, start = 16.dp, end = 16.dp)
    ) {
        Text(text = "Daftar Akun", style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.nim,
            onValueChange = viewModel::onNimChanged,
            label = { Text("NIM") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.nimError != null,
            supportingText = {
                if (uiState.nimError != null) {
                    Text(text = uiState.nimError!!, color = MaterialTheme.colorScheme.error)
                }
            }
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.nama,
            onValueChange = viewModel::onNamaChanged,
            label = { Text("Nama") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.namaError != null,
            supportingText = {
                if (uiState.namaError != null) {
                    Text(text = uiState.namaError!!, color = MaterialTheme.colorScheme.error)
                }
            }
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = uiState.password,
            onValueChange = viewModel::onPasswordChanged,
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            isError = uiState.passwordError != null,
            supportingText = {
                if (uiState.passwordError != null) {
                    Text(text = uiState.passwordError!!, color = MaterialTheme.colorScheme.error)
                }
            },
            visualTransformation = if (uiState.passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (uiState.passwordVisible) Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff
                IconButton(onClick = viewModel::onTogglePasswordVisibility) {
                    Icon(imageVector = image, contentDescription = null)
                }
            }
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = viewModel::onRegisterClicked,
            enabled = !uiState.isLoading, // Disable button when loading
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50) // Green color from V1.0
            )
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.White
                )
            } else {
                Text(text = "Daftar", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}