package com.likeminds.usertagging.util

import android.content.res.Resources
import com.likeminds.usertagging.util.generator.ColorGenerator
import com.likeminds.usertagging.util.generator.TextDrawable

object ImageUtils {
    val SIXTY_PX = dpToPx(60)

    /**
     * To create a drawable with Initials of Name
     */
    fun getNameDrawable(
        size: Int,
        id: String?,
        name: String?,
        circle: Boolean? = false,
        roundRect: Boolean? = false
    ): Pair<TextDrawable, Int> {
        val uniqueId = id ?: name ?: "LM"
        val nameCode = getNameInitial(name)
        val color = ColorGenerator.MATERIAL.getColor(uniqueId)
        val builder =
            TextDrawable.builder().beginConfig().bold().height(size).width(size).endConfig()
        val drawable = when {
            circle == true -> {
                builder.buildRound(nameCode, color)
            }

            roundRect == true -> {
                builder.buildRoundRect(nameCode, color, dpToPx(10))
            }

            else -> {
                builder.buildRect(nameCode, color)
            }
        }
        return Pair(drawable, color)
    }

    //returns initials of the name
    private fun getNameInitial(
        userName: String?
    ): String {
        val name = userName?.trim()
        if (name.isNullOrEmpty()) {
            return "U"
        }
        if (!name.contains(" ")) {
            return name[0].uppercase()
        }
        val nameParts = name.split(" ").map { it.trim() }
        return "${nameParts.first()[0].uppercase()}${nameParts.last()[0].uppercase()}"
    }

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }
}