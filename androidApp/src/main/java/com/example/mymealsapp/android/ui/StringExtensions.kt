package com.example.mymealsapp.android.ui

import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

fun String.convertToParagraph(): String {
    val body = this
        .replace("\r", "")
        .replace("\n", "")
    val stringList = body.split(".")
    val bulletPointsText = buildAnnotatedString {
        stringList.forEach { item ->
            if (item.isNotEmpty()) {
                withStyle(style = ParagraphStyle()) {
                    append("\u2022 ")
                    append(item)
                    append(".")
                }
                append("\n")
            }
        }
    }
    return bulletPointsText.toString()
}

fun String.getTitleBold(value: String) =
    buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontWeight = FontWeight.Bold
            )
        ) {
            append(this@getTitleBold)
        }
        append(" ")
        append(value)
    }