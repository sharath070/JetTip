package com.sharath070.jettip.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


val iconButtonSizeModifier = Modifier.size(40.dp)

@Composable
fun RoundIconButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    tint: Color = Color.Black.copy(alpha = 0.8f),
    bgColor: Color = MaterialTheme.colorScheme.background,
    elevation: Dp = 4.dp,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .clickable { onClick.invoke() }
            .then(iconButtonSizeModifier),
        shape = CircleShape,
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(elevation)
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = "plus or minus",
            tint = tint,
            modifier = Modifier.fillMaxSize()
        )
    }
}