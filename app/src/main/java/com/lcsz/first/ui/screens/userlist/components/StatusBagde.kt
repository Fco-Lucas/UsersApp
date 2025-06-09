package com.lcsz.first.ui.screens.userlist.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun StatusBadge(
    status: String
) {
    val (backgroundColor, textColor, text) = when(status) {
        "ACTIVE" -> Triple(Color(0xFF008000), Color.White, "Ativo")
        "INACTIVE" -> Triple(Color.Red, Color.White, "Inativo")
        else -> Triple(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.onSecondaryContainer,
            "Inválido"
        )
    }

    Surface(
        shape = MaterialTheme.shapes.extraLarge,
        color = backgroundColor,
        contentColor = textColor,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}

@Preview
@Composable
private fun StatusBadgeInactivePreview() {
    StatusBadge(status = "INACTIVE")
}

@Preview
@Composable
private fun StatusBadgeActivePreview() {
    StatusBadge("ACTIVE")
}