package com.likeminds.usertagging.util

interface TextWatcherListener {

    fun hitTaggingApi(text: String)

    fun onUserTagRemoved(regex: String)

    fun hideSuggestionList()
}