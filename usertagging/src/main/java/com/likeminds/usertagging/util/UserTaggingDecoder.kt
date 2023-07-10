package com.likeminds.usertagging.util

import android.net.Uri

object UserTaggingDecoder {
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
}