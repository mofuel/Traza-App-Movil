package com.bancocloud.traza.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.bancocloud.traza.core.designsystem.theme.TrazaColors
import com.bancocloud.traza.core.designsystem.theme.TrazaSpacing

@Composable
fun TrazaEmptyDestination(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(TrazaSpacing.Space4),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = TrazaColors.Surface),
            border = BorderStroke(width = TrazaSpacing.Space1 / 4, color = TrazaColors.Border),
            shape = MaterialTheme.shapes.medium,
        ) {
            Column(
                modifier = Modifier
                    .padding(TrazaSpacing.Space5),
                verticalArrangement = Arrangement.spacedBy(TrazaSpacing.Space2),
            ) {
                Text(
                    text = title,
                    color = TrazaColors.TextPrimary,
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = message,
                    color = TrazaColors.TextSecondary,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
