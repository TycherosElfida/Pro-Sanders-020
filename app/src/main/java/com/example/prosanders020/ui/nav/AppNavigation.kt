package com.example.prosanders020.ui.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.prosanders020.ui.screen.LoginScreen
import com.example.prosanders020.ui.screen.RegisterScreen
import com.example.prosanders020.ui.screen.UserListScreen
import kotlinx.serialization.Serializable

@Serializable
object RouteLogin

@Serializable
object RouteRegister

@Serializable
object RouteUserList

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: Any = RouteLogin // App starts at Login
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<RouteLogin> {
            LoginScreen(
                viewModel = hiltViewModel(), // Hilt provides the VM
                onLoginSuccess = {
                    navController.navigate(RouteUserList) {
                        popUpTo(RouteLogin) { inclusive = true } // Clear login from back stack
                    }
                },
                onRegisterClick = { navController.navigate(RouteRegister) }
            )
        }

        composable<RouteRegister> {
            RegisterScreen(
                viewModel = hiltViewModel(),
                onRegisterSuccess = {
                    navController.navigate(RouteLogin) {
                        popUpTo(RouteRegister) { inclusive = true } // Go back to Login
                    }
                }
            )
        }

        composable<RouteUserList> {
            UserListScreen(
                viewModel = hiltViewModel(),
                onNavigateBackToLogin = {
                    navController.navigate(RouteLogin) {
                        popUpTo(RouteUserList) { inclusive = true } // Clear list from back stack
                    }
                }
            )
        }
    }
}