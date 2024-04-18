package com.likeminds.usertagging

import android.net.Uri

fun interface UserTaggingDecoderListener {
    fun onTagClick(tag: Uri)
}