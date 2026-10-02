package com.bancocloud.traza.feature.home.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bancocloud.traza.R
import com.bancocloud.traza.core.designsystem.component.TrazaEmptyDestination

@Composable
fun HomeScreen() {
    TrazaEmptyDestination(
        title = stringResource(R.string.destination_home),
        message = stringResource(R.string.empty_destination_message),
    )
}
