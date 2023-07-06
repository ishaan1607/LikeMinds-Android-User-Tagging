package com.likeminds.usertagging.util

import com.likeminds.usertagging.model.TagUser

interface UserTaggingViewListener {

    fun onMemberTagged(user: TagUser) {
        //when a member is tagged from list
    }

    fun onMemberRemoved(user: TagUser) {
        //when a tagged member is removed from the text
    }

    fun onShow() {
        //to show suggestion list
    }

    fun onHide() {
        //to hide suggestion list
    }

    fun callApi(page: Int, searchName: String) {
        //call tagging api
    }
}