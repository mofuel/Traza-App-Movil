package com.bancocloud.traza.core.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bancocloud.traza.core.designsystem.theme.TrazaColors
import com.bancocloud.traza.core.designsystem.theme.TrazaSpacing

@Composable
fun TrazaTopBar(
    brand: String,
    title: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = TrazaColors.Surface,
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
                    .padding(horizontal = TrazaSpacing.Space4),
                horizontalArrangement = Arrangement.spacedBy(TrazaSpacing.Space3),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(32.dp)
                        .background(TrazaColors.BrandPrimary),
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = brand,
                        color = TrazaColors.TextPrimary,
                        style = MaterialTheme.typography.labelSmall,
                    )
                    Text(
                        text = title,
                        color = TrazaColors.TextPrimary,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
                if (actionLabel != null && onAction != null) {
                    TextButton(onClick = onAction) {
                        Text(
                            text = actionLabel,
                            color = TrazaColors.TextPrimary,
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }
            }
            HorizontalDivider(color = TrazaColors.Border)
        }
    }
}

@Composable
fun TrazaBottomBar(
    items: List<TrazaNavigationItem>,
    selectedRoute: String,
    onItemSelected: (TrazaNavigationItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = TrazaColors.Surface,
    ) {
        Column {
            HorizontalDivider(color = TrazaColors.Border)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .selectableGroup(),
            ) {
                items.forEach { item ->
                    val selected = item.route == selectedRoute
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(
                                if (selected) TrazaColors.BrandSoft else TrazaColors.Surface,
                            )
                            .selectable(
                                selected = selected,
                                onClick = { onItemSelected(item) },
                                role = Role.Tab,
                            )
                            .padding(horizontal = TrazaSpacing.Space1),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Box(
                            modifier = Modifier
                                .width(32.dp)
                                .height(3.dp)
                                .background(
                                    if (selected) {
                                        TrazaColors.BrandPrimary
                                    } else {
                                        TrazaColors.Surface
                                    },
                                ),
                        )
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = item.label,
                                color = if (selected) {
                                    TrazaColors.TextPrimary
                                } else {
                                    TrazaColors.TextSecondary
                                },
                                style = MaterialTheme.typography.labelSmall,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
        }
    }
}

data class TrazaNavigationItem(
    val route: String,
    val label: String,
)
