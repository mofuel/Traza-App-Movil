package com.bancocloud.traza.feature.onboarding.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bancocloud.traza.R
import com.bancocloud.traza.core.designsystem.component.TrazaEmptyDestination

@Composable
fun OnboardingScreen() {
    TrazaEmptyDestination(
        title = stringResource(R.string.destination_onboarding),
        message = stringResource(R.string.empty_destination_message),
    )
}
