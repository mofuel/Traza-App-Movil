package com.bancocloud.traza.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bancocloud.traza.R
import com.bancocloud.traza.app.navigation.TrazaDestination
import com.bancocloud.traza.app.navigation.TrazaNavHost
import com.bancocloud.traza.core.designsystem.component.TrazaBottomBar
import com.bancocloud.traza.core.designsystem.component.TrazaNavigationItem
import com.bancocloud.traza.core.designsystem.component.TrazaTopBar
import com.bancocloud.traza.core.designsystem.theme.TrazaColors
import com.bancocloud.traza.feature.auth.presentation.AuthUiState
import com.bancocloud.traza.feature.auth.presentation.AuthViewModel
import com.bancocloud.traza.feature.auth.presentation.LoginScreen
import com.bancocloud.traza.feature.auth.presentation.SessionInitializingScreen

@Composable
fun TrazaAdminApp(container: AppContainer) {
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModel.factory(container.authRepository, container.sessionManager),
    )
    val authState by authViewModel.uiState.collectAsStateWithLifecycle()

    when (val state = authState) {
        AuthUiState.Initializing -> SessionInitializingScreen()
        is AuthUiState.Authenticated -> AuthenticatedTrazaAdminApp(
            onLogout = authViewModel::logout,
        )
        AuthUiState.Authenticating -> LoginScreen(
            isLoading = true,
            message = null,
            onLogin = authViewModel::login,
        )
        is AuthUiState.Error -> LoginScreen(
            isLoading = false,
            message = state.message,
            onLogin = authViewModel::login,
        )
        AuthUiState.Unauthenticated -> LoginScreen(
            isLoading = false,
            message = null,
            onLogin = authViewModel::login,
        )
    }
}

@Composable
private fun AuthenticatedTrazaAdminApp(onLogout: () -> Unit) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = TrazaDestination.fromRoute(backStackEntry?.destination?.route)
    val navigationItems = TrazaDestination.topLevelEntries.map { destination ->
        TrazaNavigationItem(
            route = destination.route,
            label = stringResource(destination.labelRes),
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = TrazaColors.AppBackground,
        topBar = {
            TrazaTopBar(
                brand = stringResource(R.string.brand_name),
                title = stringResource(currentDestination.labelRes),
                actionLabel = stringResource(R.string.logout_action),
                onAction = onLogout,
            )
        },
        bottomBar = {
            TrazaBottomBar(
                items = navigationItems,
                selectedRoute = currentDestination.route,
                onItemSelected = { item ->
                    if (item.route != currentDestination.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
            )
        },
    ) { innerPadding ->
        TrazaNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
        )
    }
}
