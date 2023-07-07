package com.likeminds.usertagging.view

import android.content.Context
import android.text.Editable
import android.text.SpannableString
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.likeminds.usertagging.R
import com.likeminds.usertagging.databinding.LayoutUserTaggingBinding
import com.likeminds.usertagging.model.TagUser
import com.likeminds.usertagging.model.UserTaggingConfig
import com.likeminds.usertagging.util.*
import com.likeminds.usertagging.view.adapter.TagUserAdapter
import com.likeminds.usertagging.view.adapter.TagUserAdapterClickListener

class UserTaggingView(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs), TextWatcherListener, TagUserAdapterClickListener {

    private var binding = LayoutUserTaggingBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    var taggingEnabled = true
        set(value) {
            field = value
            enableTagging(value)
        }

    private lateinit var mAdapter: TagUserAdapter
    private lateinit var userTaggingTextWatcher: UserTaggingTextWatcher
    private var userTaggingViewListener: UserTaggingViewListener? = null
    private lateinit var scrollListener: EndlessRecyclerScrollListener

    //Community Members and groups to search
    private val communityMembersAndGroups = mutableListOf<TagUser>()

    //contains searched text
    private var searchText: String = ""

    //Contains selected members
    private val selectedMembers by lazy { mutableListOf<TagUser>() }

    private lateinit var config: UserTaggingConfig

    val isShowing: Boolean
        get() {
            return mAdapter.itemCount > 0
        }

    /**
     * Initialises member tagging view with the required attribute and start listening for member tagging
     * @param config [UserTaggingConfig] Contains configurable fields
     */
    fun initialize(config: UserTaggingConfig) {
        this.config = config
        initializeRecyclerView()
        initializeTextWatcher()
        configureView()
    }

    private fun configureView() {
        //Set max height
        val heightInPx = UserTaggingUtil.getMaxHeight(context, config.maxHeightInPercentage)
        maxHeight = heightInPx
        val lp = binding.recyclerView.layoutParams as LayoutParams
        lp.matchConstraintMaxHeight = heightInPx
        binding.recyclerView.layoutParams = lp

        //Set the theme
        if (config.darkMode) {
            binding.constraintLayout.setBackgroundResource(R.color.black_80)
        } else {
            binding.constraintLayout.setBackgroundResource(R.drawable.background_container)
        }
    }

    fun reSetMaxHeight(px: Int) {
        if (px >= maxHeight || px <= 0) {
            return
        }
        maxHeight = px
        val lp = binding.recyclerView.layoutParams as LayoutParams
        lp.matchConstraintMaxHeight = px
        binding.recyclerView.layoutParams = lp
    }

    fun addListener(userTaggingViewListener: UserTaggingViewListener) {
        this.userTaggingViewListener = userTaggingViewListener
    }

    private fun enableTagging(value: Boolean) {
        if (value) {
            userTaggingTextWatcher.startObserving()
        } else {
            userTaggingTextWatcher.stopObserving()
        }
    }

    private fun initializeTextWatcher() {
        userTaggingTextWatcher = UserTaggingTextWatcher(taggingEnabled, config.editText)
        userTaggingTextWatcher.addTextWatcherListener(this)
        userTaggingTextWatcher.startObserving()
    }

    private fun initializeRecyclerView() {
        //create adapter
        mAdapter = TagUserAdapter(config.darkMode, this)

        //create layout manager
        val linearLayoutManager = LinearLayoutManager(this.context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        scrollListener = object : EndlessRecyclerScrollListener(linearLayoutManager) {
            override fun onLoadMore(currentPage: Int) {
                if (currentPage > 0) {
                    userTaggingViewListener?.callApi(currentPage, searchText)
                }
            }
        }
        scrollListener.resetData()

        binding.recyclerView.apply {
            adapter = mAdapter
            layoutManager = linearLayoutManager
            addOnScrollListener(scrollListener)
        }
    }

    fun hide() {
        if (isShowing) {
            mAdapter.clear()
            userTaggingViewListener?.onHide()
        }
    }

    private fun getMemberFromSelectedList(userUniqueId: String): TagUser? {
        return selectedMembers.firstOrNull { member ->
            member.userUniqueId == userUniqueId
        }
    }

    private fun getMember(userUniqueId: String): TagUser? {
        return communityMembersAndGroups.firstOrNull { member ->
            member.userUniqueId == userUniqueId
        }
    }

    private fun showMemberTaggingList() {
        if (communityMembersAndGroups.isNotEmpty()) {
            userTaggingViewListener?.onShow()
            val lastItem = communityMembersAndGroups.lastOrNull()
            mAdapter.setMembers(communityMembersAndGroups.map {
                if (it.userUniqueId == lastItem?.userUniqueId) {
                    //if last item hide bottom line in item view
                    it.toBuilder().isLastItem(true).build()
                } else {
                    it
                }
            })
        } else {
            hide()
        }
    }

    override fun onHitTaggingApi(text: String) {
        searchText = text.substring(1) //omit '@'
        scrollListener.resetData()
        userTaggingViewListener?.callApi(1, searchText)
    }

    override fun onMemberRemoved(regex: String) {
        val memberRoute = UserTaggingDecoder.getRouteFromRegex(regex) ?: return
        val userUniqueId = memberRoute.lastPathSegment ?: return
        val member = getMemberFromSelectedList(userUniqueId)
        if (member != null) {
            selectedMembers.remove(member)
            userTaggingViewListener?.onMemberRemoved(member)
        }
    }

    override fun dismissMemberTagging() {
        hide()
    }

    override fun onMemberTagged(user: TagUser) {
        val tagSpannableString = "@${user.name}"
        val memberName = SpannableString(tagSpannableString)

        //if id == 0, then it is a group tag
        val regex = if (user.id == 0) {
            //get route from object
            user.tag
        } else {
            //create regex from name and id
            "<<${user.name}|route://user_profile/${user.id}>>"
        }

        //set span
        memberName.setSpan(
            UserTaggingClickableSpan(
                config.color,
                regex
            ), 0, memberName.length, 0
        )
        val selectedMember = getMemberFromSelectedList(user.userUniqueId)
        if (selectedMember == null) {
            selectedMembers.add(user)
        }
        userTaggingTextWatcher.replaceEditText(memberName)
        userTaggingTextWatcher.resetGlobalPosition()
        userTaggingViewListener?.onMemberTagged(user)
        hide()
    }

    fun decodeFirstMemberAndAddToSelectedList(text: String?): String? {
        UserTaggingDecoder.decode(
            config.editText,
            text,
            config.color
        )
        val firstMember = UserTaggingDecoder.decodeAndReturnAllTaggedMembers(text).firstOrNull()
            ?: return null
        val member = getMember(firstMember.first) ?: return null
        if (getMemberFromSelectedList(member.userUniqueId) == null) {
            selectedMembers.add(member)
        }
        return member.name
    }

    fun replaceSelectedMembers(editable: Editable?): String {
        if (editable == null) {
            return ""
        }
        val spans = UserTaggingUtil.getSortedSpan(editable)
        val stringBuilder = StringBuilder()
        var lastIndex = 0
        spans.forEach { span ->
            val start = editable.getSpanStart(span)
            val end = editable.getSpanEnd(span)
            if (start > lastIndex) {
                stringBuilder.append(editable.substring(lastIndex, start))
            }
            stringBuilder.append(span.regex)
            lastIndex = end
        }
        if (editable.length >= lastIndex) {
            stringBuilder.append(editable.substring(lastIndex))
        }
        return stringBuilder.toString()
    }

    fun setMembersAndGroup(usersAndGroups: ArrayList<TagUser>) {
        communityMembersAndGroups.clear()
        communityMembersAndGroups.addAll(usersAndGroups)
        showMemberTaggingList()
    }

    fun addMembers(usersAndGroups: ArrayList<TagUser>) {
        communityMembersAndGroups.addAll(usersAndGroups)
        if (isShowing) {
            mAdapter.allMembers(usersAndGroups)
        }
    }

    fun getTaggedMemberCount() = selectedMembers.size

    fun getTaggedMembers() = selectedMembers.toList()

    fun isMembersListEmpty() = communityMembersAndGroups.isEmpty()
}