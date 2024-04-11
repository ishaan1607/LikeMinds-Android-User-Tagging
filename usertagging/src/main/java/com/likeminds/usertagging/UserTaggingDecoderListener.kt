package com.likeminds.usertagging

import android.net.Uri

interface UserTaggingDecoderListener {
    fun onTagClick(tag: Uri)
}