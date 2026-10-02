package com.bancocloud.traza.feature.dataops.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bancocloud.traza.R
import com.bancocloud.traza.core.designsystem.component.TrazaEmptyDestination

@Composable
fun DataOpsScreen() {
    TrazaEmptyDestination(
        title = stringResource(R.string.destination_data_ops),
        message = stringResource(R.string.empty_destination_message),
    )
}
