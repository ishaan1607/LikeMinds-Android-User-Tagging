package com.likeminds.usertagging.util

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

class UserTaggingClickableSpan(
    val color: Int,
    val regex: String,
    val underLineText: Boolean = false,
    val userTaggingClickableSpanListener: UserTaggingClickableSpanListener? = null
) : ClickableSpan() {

    override fun updateDrawState(textPaint: TextPaint) {
        super.updateDrawState(textPaint)
        try {
            textPaint.color = color
            textPaint.isUnderlineText = underLineText
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onClick(widget: View) {
        userTaggingClickableSpanListener?.onClick(regex)
    }

    fun getMemberId(): String? {
        return UserTaggingDecoder.getMemberIdFromRegex(regex)
    }
}
