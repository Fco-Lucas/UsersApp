package com.lcsz.first.util.masks

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CpfVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // text.text aqui são os dígitos puros do CPF (ex: "123", "123456", "12345678900")
        // Garante que estamos trabalhando com no máximo 11 dígitos
        val trimmed = text.text.take(11)

        val formattedText = buildString {
            for (i in trimmed.indices) {
                append(trimmed[i])
                if (i == 2 || i == 5) { // Após o 3º e 6º dígito
                    if (i < trimmed.length - 1) { // Adiciona '.' apenas se houver mais dígitos a seguir
                        append('.')
                    }
                } else if (i == 8) { // Após o 9º dígito
                    if (i < trimmed.length - 1) { // Adiciona '-' apenas se houver mais dígitos a seguir
                        append('-')
                    }
                }
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // offset é a posição do cursor no texto original (dígitos puros)
                var punctuationCount = 0
                if (offset > 2) punctuationCount++ // Passou da posição do primeiro '.'
                if (offset > 5) punctuationCount++ // Passou da posição do segundo '.'
                if (offset > 8) punctuationCount++ // Passou da posição do '-'

                // O deslocamento transformado é o original + os separadores inseridos ANTES dele.
                // Importante: O resultado deve ser limitado ao comprimento ATUAL do texto formatado.
                return (offset + punctuationCount).coerceAtMost(formattedText.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                // offset é a posição do cursor no texto formatado (com máscara)
                var punctuationCount = 0
                // Contamos quantos separadores existem ATÉ a posição do cursor no texto formatado.
                // Posições dos separadores no texto formatado: 3 (.), 7 (.), 11 (-)
                if (offset > 3) punctuationCount++  // Cursor está depois do primeiro '.'
                if (offset > 7) punctuationCount++  // Cursor está depois do segundo '.'
                if (offset > 11) punctuationCount++ // Cursor está depois do '-'

                // O deslocamento original é o transformado - os separadores antes dele.
                // Importante: O resultado deve ser limitado ao comprimento ATUAL do texto original (trimmed).
                return (offset - punctuationCount).coerceAtMost(trimmed.length)
            }
        }

        return TransformedText(AnnotatedString(formattedText), offsetMapping)
    }
}