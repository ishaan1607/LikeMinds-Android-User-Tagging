package com.likeminds.usertagging.view.adapter

import com.likeminds.usertagging.model.TagUser

internal interface TagUserAdapterClickListener {

    fun onUserTagged(user: TagUser)
}