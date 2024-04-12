package com.likeminds.usertagging.model

import android.widget.EditText
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.FloatRange
import com.likeminds.usertagging.R

class UserTaggingConfig private constructor(
    val editText: EditText,
    @FloatRange(from = 0.0, to = 1.0) val maxHeightInPercentage: Float = 0.4f,
    @ColorRes val color: Int,
    val hasAtRateSymbol: Boolean
) {
    class Builder {
        private lateinit var edittext: EditText

        @FloatRange(from = 0.0, to = 1.0)
        private var maxHeightInPercentage: Float = 0.4f

        @ColorRes
        private var color: Int = R.color.pure_blue
        private var hasAtRateSymbol: Boolean = true

        fun editText(editText: EditText) = apply { this.edittext = editText }

        fun maxHeightInPercentage(
            @FloatRange(from = 0.0, to = 1.0)
            maxHeightInPercentage: Float,
        ) = apply {
            this.maxHeightInPercentage = maxHeightInPercentage
        }

        fun color(@ColorRes color: Int) = apply {
            this.color = color
        }

        fun hasAtRateSymbol(hasAtRateSymbol: Boolean) =
            apply { this.hasAtRateSymbol = hasAtRateSymbol }

        fun build(): UserTaggingConfig {
            return UserTaggingConfig(edittext, maxHeightInPercentage, color, hasAtRateSymbol)
        }
    }

    fun toBuilder(): Builder {
        return Builder().editText(editText)
            .maxHeightInPercentage(maxHeightInPercentage)
            .color(color)
            .hasAtRateSymbol(hasAtRateSymbol)
    }
}