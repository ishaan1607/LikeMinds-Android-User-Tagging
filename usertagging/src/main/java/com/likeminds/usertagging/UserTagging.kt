package com.likeminds.usertagging

import com.likeminds.usertagging.model.UserTaggingConfig
import com.likeminds.usertagging.util.UserTaggingViewListener
import com.likeminds.usertagging.view.UserTaggingSuggestionListView

object UserTagging {
    /***
     * Use this function to initiate the user tagging library in your activity/fragment
     *
     * @param userTaggingSuggestionListView: Instance of [UserTaggingSuggestionListView]
     * @param config: object of [UserTaggingConfig]
     * @param listener: object of [UserTaggingViewListener] to getting updates.
     */
    fun initialize(
        userTaggingSuggestionListView: UserTaggingSuggestionListView,
        config: UserTaggingConfig,
        listener: UserTaggingViewListener
    ) {
        userTaggingSuggestionListView.initialize(config)
        userTaggingSuggestionListView.addListener(listener)
    }
}