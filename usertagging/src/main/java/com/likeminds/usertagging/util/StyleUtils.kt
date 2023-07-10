package com.likeminds.usertagging.util

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat

object StyleUtils {
    private val resourceMap: MutableMap<Int, Typeface> = HashMap()

    fun getFont(context: Context, @FontRes fontRes: Int): Typeface? {
        if (fontRes in resourceMap) {
            return resourceMap[fontRes]
        }

        val typeface = safeLoadTypeface(context, fontRes) ?: return null
        resourceMap[fontRes] = typeface
        return typeface
    }

    private fun safeLoadTypeface(context: Context, @FontRes fontRes: Int): Typeface? {
        return try {
            ResourcesCompat.getFont(context, fontRes)
        } catch (t: Throwable) {
            Log.e("UserTagging", "safeLoadTypeface failed:$t")
            null
        }
    }
}