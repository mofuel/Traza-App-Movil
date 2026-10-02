package com.bancocloud.traza.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.bancocloud.traza.feature.copilot.presentation.CopilotScreen
import com.bancocloud.traza.feature.dataops.presentation.DataOpsScreen
import com.bancocloud.traza.feature.home.presentation.HomeScreen
import com.bancocloud.traza.feature.onboarding.presentation.OnboardingScreen
import com.bancocloud.traza.feature.operations.presentation.OperationsScreen

@Composable
fun TrazaNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = TrazaDestination.Home.route,
        modifier = modifier,
    ) {
        composable(TrazaDestination.Home.route) { HomeScreen() }
        composable(TrazaDestination.Onboarding.route) { OnboardingScreen() }
        composable(TrazaDestination.Operations.route) { OperationsScreen() }
        composable(TrazaDestination.DataOps.route) { DataOpsScreen() }
        composable(TrazaDestination.Copilot.route) { CopilotScreen() }
    }
}
