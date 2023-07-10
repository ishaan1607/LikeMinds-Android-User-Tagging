package com.likeminds.usertagging

import com.likeminds.usertagging.model.UserTaggingConfig
import com.likeminds.usertagging.util.UserTaggingViewListener
import com.likeminds.usertagging.view.UserTaggingSuggestionListView

object UserTagging {
    fun initialize(
        userTaggingSuggestionListView: UserTaggingSuggestionListView,
        config: UserTaggingConfig,
        listener: UserTaggingViewListener
    ) {
        userTaggingSuggestionListView.initialize(config)
        userTaggingSuggestionListView.addListener(listener)
    }
}