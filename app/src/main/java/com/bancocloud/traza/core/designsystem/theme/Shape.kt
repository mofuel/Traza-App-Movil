package com.bancocloud.traza.core.designsystem.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

object TrazaShapes {
    val Control = RoundedCornerShape(10.dp)
    val Card = RoundedCornerShape(16.dp)
    val FeaturedCard = RoundedCornerShape(18.dp)
    val Pill: Shape = CircleShape
}

internal val TrazaMaterialShapes = Shapes(
    small = TrazaShapes.Control,
    medium = TrazaShapes.Card,
    large = TrazaShapes.FeaturedCard,
)
