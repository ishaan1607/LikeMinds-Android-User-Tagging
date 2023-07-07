package com.likeminds.usertagging.util

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.text.Editable
import android.util.DisplayMetrics
import android.view.WindowInsets
import androidx.annotation.FloatRange
import com.likeminds.usertagging.model.TagUser
import com.likeminds.usertagging.view.UserTaggingView

object UserTaggingUtil {

    const val PAGE_SIZE = 20

    /**
     * handles result and set result to [memberTagging] view as per [page]
     * */
    fun setMembersInView(
        memberTagging: UserTaggingView,
        result: Pair<Int, ArrayList<TagUser>>?
    ) {
        if (result != null) {
            val page = result.first
            val list = result.second
            if (page == 1) {
                //clear and set in adapter
                memberTagging.setMembersAndGroup(list)
            } else {
                //add to the adapter
                memberTagging.addMembers(list)
            }
        } else {
            return
        }
    }

    private const val DEFAULT_MAX_HEIGHT = 300

    @JvmSynthetic
    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    @JvmSynthetic
    fun getMaxHeight(
        context: Context,
        @FloatRange(from = 0.0, to = 1.0) percentage: Float
    ): Int {
        val activity = context as? Activity ?: return dpToPx(DEFAULT_MAX_HEIGHT)
        return (getDeviceHeight(activity) * percentage).toInt()
    }

    @Suppress("DEPRECATION")
    private fun getDeviceHeight(activity: Activity): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = activity.windowManager.currentWindowMetrics
            val insets =
                windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
            windowMetrics.bounds.height() - insets.top - insets.bottom
        } else {
            val displayMetrics = DisplayMetrics()
            activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
            displayMetrics.heightPixels
        }
    }

    @JvmSynthetic
    fun getLastSpan(
        editable: Editable,
        spans: Array<UserTaggingClickableSpan>
    ): UserTaggingClickableSpan {
        if (spans.size == 1) {
            return spans[0]
        }
        return spans.maxByOrNull {
            editable.getSpanEnd(it)
        }!!
    }

    @JvmSynthetic
    fun getSortedSpan(editable: Editable): List<UserTaggingClickableSpan> {
        return editable.getSpans(0, editable.length, UserTaggingClickableSpan::class.java)
            .sortedBy {
                editable.getSpanStart(it)
            }
    }
}