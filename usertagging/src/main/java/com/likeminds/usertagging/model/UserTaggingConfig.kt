package com.likeminds.usertagging.model

import android.widget.EditText
import androidx.annotation.FloatRange

class UserTaggingConfig private constructor(
    val editText: EditText,
    @FloatRange(from = 0.0, to = 1.0) val maxHeightInPercentage: Float = 0.4f,
    val color: Int,
) {
    class Builder {
        private lateinit var edittext: EditText
        @FloatRange(from = 0.0, to = 1.0)
        private var maxHeightInPercentage: Float = 0.4f
        private var color: Int = -1

        fun editText(editText: EditText) = apply { this.edittext = editText }

        fun maxHeightInPercentage(
            @FloatRange(from = 0.0, to = 1.0)
            maxHeightInPercentage: Float,
        ) = apply {
            this.maxHeightInPercentage = maxHeightInPercentage
        }

        fun color(color: Int) = apply {
            this.color = color
        }

        fun build(): UserTaggingConfig {
            if (this::edittext.isInitialized) {
                throw Error("editText is a required attribute")
            }
            return UserTaggingConfig(edittext, maxHeightInPercentage, color)
        }
    }

    fun toBuilder(): Builder {
        return Builder().editText(editText)
            .maxHeightInPercentage(maxHeightInPercentage)
            .color(color)
    }
}