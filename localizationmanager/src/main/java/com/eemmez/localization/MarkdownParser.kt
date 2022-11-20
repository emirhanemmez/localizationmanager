package com.eemmez.localization

import android.content.Context
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import androidx.annotation.ColorRes

internal class MarkdownParser(private val context: Context) {
    private var cursor = 0
    private var afterLastNode = 0
    private var clickableTextColor: Int? = 0
    private var clickHandlers = mutableMapOf<String, () -> Unit>()

    fun buildSpannable(rawInput: String): Spannable {
        cursor = 0
        afterLastNode = 0
        return parse(rawInput)
    }

    fun onClick(id: String, @ColorRes clickableTextColor: Int?, handler: () -> Unit) {
        this.clickableTextColor = clickableTextColor
        clickHandlers[id] = handler
    }

    fun clearClickHandlers() {
        clickHandlers.clear()
    }

    private fun isEndOfInput(rawInput: String): Boolean {
        return cursor == rawInput.length
    }

    private fun parse(rawInput: String): Spannable {
        val spannableStringBuilder = SpannableStringBuilder()
        if (rawInput.isEmpty()) {
            return SpannableString("")
        }

        var boldCount = 0

        while (!isEndOfInput(rawInput)) {
            when (peekCharacter(rawInput)) {
                '*' -> {
                    if (peekNextCharacter(rawInput) == '*') {
                        boldCount += 1
                        consumeText(rawInput, spannableStringBuilder)
                        skipCharacter(rawInput)
                        skipCharacter(rawInput)

                        if (parseBold(rawInput, spannableStringBuilder)) {
                            afterLastNode = cursor
                        }
                    } else {
                        skipCharacter(rawInput)
                    }
                }

                '[' -> {
                    handleLink(rawInput, spannableStringBuilder)
                }
                else -> skipCharacter(rawInput)
            }
        }

        consumeText(rawInput, spannableStringBuilder)
        return spannableStringBuilder
    }

    private fun parseBold(
        rawInput: String,
        spannableStringBuilder: SpannableStringBuilder
    ): Boolean {
        val beginning = cursor
        while (!isEndOfInput(rawInput)) {
            if (peekCharacter(rawInput) == '*' && peekNextCharacter(rawInput) == '*') {
                val cleanText = rawInput.substring(beginning, cursor)
                val bold = StyleSpan(Typeface.BOLD)
                spannableStringBuilder.append(cleanText, bold, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                skipCharacter(rawInput)
                skipCharacter(rawInput)
                return true
            }

            skipCharacter(rawInput)
        }

        return false
    }

    private fun handleLink(rawInput: String, spannableStringBuilder: SpannableStringBuilder) {
        consumeText(rawInput, spannableStringBuilder)
        skipCharacter(rawInput)

        val linkLabel = parseLinkLabel(rawInput)
        if (linkLabel != null && parseLink(linkLabel, rawInput, spannableStringBuilder)) {
            afterLastNode = cursor
        } else {
            skipCharacter(rawInput)
        }
    }

    private fun parseLinkLabel(rawInput: String): String? {
        val beginning = cursor
        while (!isEndOfInput(rawInput)) {
            if (peekCharacter(rawInput) == ']') {
                val cleanText = rawInput.substring(beginning, cursor)
                skipCharacter(rawInput)
                return cleanText
            }

            skipCharacter(rawInput)
        }

        return null
    }

    private fun parseLink(
        label: String,
        rawInput: String,
        spannableStringBuilder: SpannableStringBuilder
    ): Boolean {
        if (peekCharacter(rawInput) != '(') {
            return false
        }

        val beginning = cursor + 1
        skipCharacter(rawInput)

        while (!isEndOfInput(rawInput)) {
            when (peekCharacter(rawInput)) {
                ')' -> {
                    val linkId = rawInput.substring(beginning, cursor)
                    val link = ClickableText(context, clickHandlers[linkId], clickableTextColor)
                    spannableStringBuilder.append(label, link, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    skipCharacter(rawInput)
                    return true
                }

                else -> skipCharacter(rawInput)
            }
        }

        return false
    }

    // Query
    private fun peekCharacter(rawInput: String): Char {
        return rawInput[cursor]
    }

    private fun peekNextCharacter(rawInput: String): Char? {
        if (cursor + 1 < rawInput.length) {
            return rawInput[cursor + 1]
        }

        return null
    }

    private fun setBeginning(rawInput: String) {
        if (!isEndOfInput(rawInput)) {
            afterLastNode = cursor + 1
        }
    }

    private fun consumeText(rawInput: String, spannableStringBuilder: SpannableStringBuilder) {
        spannableStringBuilder.append(rawInput.substring(afterLastNode, cursor))
        setBeginning(rawInput)
    }

    private fun skipCharacter(rawInput: String) {
        if (!isEndOfInput(rawInput)) {
            cursor += 1
        }
    }
}