package com.eemmez.localization

import android.content.Context
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

internal class ClickableText(
    private val context: Context,
    private val handler: (() -> Unit)?,
    @ColorRes private val clickableTextColor: Int?
) : ClickableSpan() {
    override fun onClick(widget: View) {
        handler?.invoke()
    }

    override fun updateDrawState(ds: TextPaint) {
        ds.isUnderlineText = false
        clickableTextColor?.let { ds.color = ContextCompat.getColor(context, it) }
    }
}