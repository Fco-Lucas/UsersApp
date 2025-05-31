package com.lcsz.first.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lcsz.first.R
import com.lcsz.first.ui.theme.Typography

@Composable
fun AppButton(
    modifier: Modifier = Modifier,
    text: String? = null,
    @DrawableRes iconRes: Int? = null,
    buttonColor: Color = Color.Blue,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.heightIn(min = 56.dp),
        shape = RoundedCornerShape(16.dp), // Arredondamento
        contentPadding = if(text == null && iconRes != null) PaddingValues(0.dp) else ButtonDefaults.ContentPadding,
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColor
        ),
        enabled = enabled,
        onClick = onClick
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            iconRes?.let {
                Icon(painter = painterResource(id = iconRes), contentDescription = "Ícone do botão")
            }
            text?.let {
                Text(text = text.uppercase(), style = Typography.labelLarge)
            }
        }
    }
}

@Preview
@Composable
private fun AppButtonPreview() {
    AppButton(
        modifier = Modifier.fillMaxWidth(),
        text = "Confirmar",
        iconRes = R.drawable.ic_scan,
        buttonColor = Color.Red
    ) {}
}

@Preview
@Composable
private fun AppButtonPreviewNoIcon() {
    AppButton(
        modifier = Modifier,
        text = "Confirmar"
    ) {}
}

@Preview
@Composable
private fun AppButtonPreviewNoText() {
    AppButton(
        modifier = Modifier,
        iconRes = R.drawable.ic_arrow_left
    ) {}
}