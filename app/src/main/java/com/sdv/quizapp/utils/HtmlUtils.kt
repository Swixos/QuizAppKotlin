package com.sdv.quizapp.utils

import android.text.Html
import android.os.Build

object HtmlUtils {
    fun decodeHtml(htmlText: String): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY).toString()
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(htmlText).toString()
        }
    }
}