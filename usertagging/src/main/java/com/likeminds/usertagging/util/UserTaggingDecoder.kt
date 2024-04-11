package com.likeminds.usertagging.util

import android.graphics.Typeface
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.TextView
import com.likeminds.usertagging.UserTaggingDecoderListener

object UserTaggingDecoder {
    /**
     * [REGEX_USER_TAGGING] is to encode/decode the route to be used for Tagging.
     * */
    private val REGEX_USER_TAGGING = Regex("<<([^<>]+\\|route://\\S+)>>")

    fun getRouteFromRegex(text: String?): Uri? {
        if (text.isNullOrEmpty()) {
            return null
        }
        val match = REGEX_USER_TAGGING.find(text, 0)
        if (match != null) {
            val value = match.value
            val tag = value.substring(2, value.length - 2).split("\\|".toRegex())
            val memberRoute = tag[1]
            return Uri.parse(memberRoute)
        }
        return null
    }

    fun decodeRegexIntoSpannableText(
        textView: TextView,
        text: String?,
        enableClick: Boolean,
        highlightColor: Int,
        underLineText: Boolean = false,
        isBold: Boolean = false,
        hasAtRateSymbol: Boolean = true,
        listener: UserTaggingDecoderListener? = null,
    ) {
        if (text.isNullOrEmpty()) {
            return
        }
        val matches = REGEX_USER_TAGGING.findAll(text, 0)
        textView.setText(text, TextView.BufferType.EDITABLE)
        matches.toList().reversed().forEach { matchResult ->
            val start = matchResult.range.first
            val end = matchResult.range.last
            val value = matchResult.value
            val tag = value.substring(2, value.length - 2).split("\\|".toRegex())
            val spannableString = if (hasAtRateSymbol) {
                "@${tag[0]}"
            } else {
                tag[0]
            }
            val memberName = SpannableString(spannableString)
            if (enableClick) {
                memberName.setSpan(
                    UserTaggingClickableSpan(
                        highlightColor,
                        value,
                        underLineText
                    ) { regex ->
                        try {
                            val tagUri = getRouteFromRegex(regex)
                                ?: return@UserTaggingClickableSpan
                            textView.setOnClickListener { return@setOnClickListener }
                            listener?.onTagClick(tagUri)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }, 0, memberName.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            } else {
                val style = if (isBold) {
                    StyleSpan(Typeface.BOLD)
                } else {
                    ForegroundColorSpan(highlightColor)
                }
                memberName.setSpan(
                    style,
                    0,
                    memberName.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            textView.setText(
                textView.editableText.replace(
                    start,
                    end + 1,
                    memberName
                ), TextView.BufferType.EDITABLE
            )
        }
    }
}