package com.likeminds.usertagging.util

interface TextWatcherListener {

    fun onHitTaggingApi(text: String)

    fun onMemberRemoved(regex: String)

    fun dismissMemberTagging()
}