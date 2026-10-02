package com.bancocloud.traza.feature.copilot.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bancocloud.traza.R
import com.bancocloud.traza.core.designsystem.component.TrazaEmptyDestination

@Composable
fun CopilotScreen() {
    TrazaEmptyDestination(
        title = stringResource(R.string.destination_copilot),
        message = stringResource(R.string.empty_destination_message),
    )
}
