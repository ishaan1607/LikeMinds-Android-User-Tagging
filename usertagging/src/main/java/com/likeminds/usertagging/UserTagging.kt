package com.likeminds.usertagging

import com.likeminds.usertagging.model.UserTaggingConfig
import com.likeminds.usertagging.util.UserTaggingViewListener
import com.likeminds.usertagging.view.UserTaggingView

object UserTagging {
    fun initialize(
        userTaggingView: UserTaggingView,
        config: UserTaggingConfig,
        listener: UserTaggingViewListener
    ) {
        userTaggingView.initialize(config)
        userTaggingView.addListener(listener)
    }
}