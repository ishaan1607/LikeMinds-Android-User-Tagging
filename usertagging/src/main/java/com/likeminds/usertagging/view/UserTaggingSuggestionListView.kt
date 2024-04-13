package com.likeminds.usertagging.view

import android.content.Context
import android.text.Editable
import android.text.SpannableString
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.likeminds.usertagging.R
import com.likeminds.usertagging.databinding.LayoutUserTaggingBinding
import com.likeminds.usertagging.model.TagUser
import com.likeminds.usertagging.model.UserTaggingConfig
import com.likeminds.usertagging.util.*
import com.likeminds.usertagging.view.adapter.TagUserAdapter
import com.likeminds.usertagging.view.adapter.TagUserAdapterClickListener

class UserTaggingSuggestionListView : ConstraintLayout, TextWatcherListener,
    TagUserAdapterClickListener {

    private var binding = LayoutUserTaggingBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )
    private lateinit var style: UserTaggingItemViewStyle

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initialize(attrs)
    }

    private fun initialize(attrs: AttributeSet?) {
        style = UserTaggingItemViewStyle(context, attrs)
    }

    private lateinit var mAdapter: TagUserAdapter
    private lateinit var userTaggingTextWatcher: UserTaggingTextWatcher
    private var userTaggingViewListener: UserTaggingViewListener? = null
    private lateinit var scrollListener: EndlessRecyclerScrollListener

    private val tagUsers = mutableListOf<TagUser>()

    //contains searched text
    private var searchText: String = ""

    //Contains selected members
    private val selectedUsers by lazy { mutableListOf<TagUser>() }

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
        val lp = binding.rvUsers.layoutParams as LayoutParams
        lp.matchConstraintMaxHeight = heightInPx
        binding.rvUsers.layoutParams = lp

        //Set the theme
        binding.constraintLayout.setBackgroundResource(R.drawable.background_container)
    }

    fun resetMaxHeight(px: Int) {
        if (px >= maxHeight || px <= 0) {
            return
        }
        maxHeight = px
        val lp = binding.rvUsers.layoutParams as LayoutParams
        lp.matchConstraintMaxHeight = px
        binding.rvUsers.layoutParams = lp
    }

    fun addListener(userTaggingViewListener: UserTaggingViewListener) {
        this.userTaggingViewListener = userTaggingViewListener
    }

    private fun initializeTextWatcher() {
        userTaggingTextWatcher = UserTaggingTextWatcher(config.editText)
        userTaggingTextWatcher.addTextWatcherListener(this)
        userTaggingTextWatcher.startObserving()
    }

    private fun initializeRecyclerView() {
        //create adapter
        mAdapter = TagUserAdapter(style, this)

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

        binding.rvUsers.apply {
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

    //get id of the already selected member
    private fun getMemberFromSelectedList(id: Int): TagUser? {
        return selectedUsers.firstOrNull { member ->
            member.id == id
        }
    }

    //display the suggestion list
    private fun showMemberTaggingList() {
        if (tagUsers.isNotEmpty()) {
            userTaggingViewListener?.onShow()
            val lastItem = tagUsers.lastOrNull()
            mAdapter.setMembers(tagUsers.map {
                if (it.id == lastItem?.id) {
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

    override fun hitTaggingApi(text: String) {
        searchText = text.substring(1) //omit '@'
        scrollListener.resetData()
        userTaggingViewListener?.callApi(1, searchText)
    }

    override fun onUserTagRemoved(regex: String) {
        val memberRoute = UserTaggingDecoder.getRouteFromRegex(regex) ?: return
        val userId = memberRoute.lastPathSegment ?: return
        val member = getMemberFromSelectedList(userId.toInt())
        if (member != null) {
            selectedUsers.remove(member)
            userTaggingViewListener?.onUserRemoved(member)
        }
    }

    override fun hideSuggestionList() {
        hide()
    }

    override fun onUserTagged(user: TagUser) {
        //check @ for this symbol
        val updatedName = if (config.hasAtRateSymbol) {
            "@${user.name}"
        } else {
            user.name
        }

        val memberName = SpannableString(updatedName)

        val regex = "<<${user.name}|route://user_profile/${user.uuid}>>"

        //set span
        memberName.setSpan(
            UserTaggingClickableSpan(
                ContextCompat.getColor(context, config.color),
                regex
            ), 0, memberName.length, 0
        )
        val selectedMember = getMemberFromSelectedList(user.id)
        if (selectedMember == null) {
            selectedUsers.add(user)
        }
        userTaggingTextWatcher.replaceEditText(memberName)
        userTaggingTextWatcher.resetGlobalPosition()
        userTaggingViewListener?.onUserTagged(user)
        hide()
    }

    //replace the selected members span with the actual route strings.
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

    //use this function to set the initial list of users
    fun setMembers(users: ArrayList<TagUser>) {
        val updatedUsers = users.map { user ->
            val nameDrawable = ImageUtils.getNameDrawable(
                ImageUtils.SIXTY_PX,
                user.id.toString(),
                user.name
            )
            user.toBuilder().placeholder(nameDrawable.first).build()
        }
        tagUsers.clear()
        tagUsers.addAll(updatedUsers)
        showMemberTaggingList()
    }

    //use this function to add the users in the existing list
    fun addMembers(users: ArrayList<TagUser>) {
        val updatedUsers = users.map { user ->
            val nameDrawable = ImageUtils.getNameDrawable(
                ImageUtils.SIXTY_PX,
                user.id.toString(),
                user.name
            )
            user.toBuilder().placeholder(nameDrawable.first).build()
        }
        tagUsers.addAll(updatedUsers)
        if (isShowing) {
            mAdapter.allMembers(users)
        }
    }

    fun getTaggedMemberCount() = selectedUsers.size

    fun getTaggedMembers() = selectedUsers.toList()

    fun isMembersListEmpty() = tagUsers.isEmpty()
}