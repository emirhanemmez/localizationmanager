package com.eemmez.localization

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.StringRes

class LocalizationManager(private val context: Context) {
    private val markdownParser = MarkdownParser(context)

    // Getter for string
    fun getString(@StringRes resId: Int): String = context.resources.getString(resId)

    // Getter for string with generic id
    fun <T> getStringByGenericId(fieldName: String, resourceClass: Class<T>): String {
        return try {
            val resId = resourceClass.getDeclaredField(fieldName).let {
                it.getInt(it)
            }
            context.resources.getString(resId)
        } catch (e: NoSuchFieldException) {
            ""
        }
    }

    // For string with multiple parameters
    fun getGenericString(
        @StringRes resId: Int,
        vararg keys: Pair<String, String>,
    ): String {
        var text = context.getString(resId)

        keys.forEach {
            text = text.replace("{${it.first}}", it.second)
        }
        return text
    }

    // For spannable string with multiple parameters
    fun getGenericSpannable(
        @StringRes resId: Int,
        vararg keys: Pair<String, String>
    ): Spannable {
        var text = context.getString(resId)

        keys.forEach {
            text = text.replace("{${it.first}}", it.second)
        }
        return markdownParser.buildSpannable(text)
    }

    // Getter for spannable string (bold, italic etc.)
    fun getSpannable(@StringRes resId: Int): Spannable =
        markdownParser.buildSpannable(context.resources.getString(resId))

    // For string with multiple linked parameters
    fun getClickableText(
        @StringRes resId: Int,
        @ColorRes clickableTextColor: Int? = null,
        textView: TextView,
        vararg clickKeys: Pair<String, () -> Unit>
    ): Spannable {
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.highlightColor = Color.TRANSPARENT
        markdownParser.clearClickHandlers()
        clickKeys.forEach {
            markdownParser.onClick(it.first, clickableTextColor, it.second)
        }
        return markdownParser.buildSpannable(getString(resId))
    }
}