package com.likeminds.usertagging.view.style

import android.content.res.TypedArray
import android.graphics.Typeface
import android.util.TypedValue
import android.widget.TextView
import androidx.annotation.AnyRes
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.annotation.StyleableRes
import com.likeminds.usertagging.util.StyleUtils

class TextStyle private constructor(
    @Px val size: Int = UNSET_SIZE,
    @ColorInt val color: Int = UNSET_COLOR,
    val style: Int = Typeface.NORMAL,
    @AnyRes val font: Int = UNSET_FONT_RESOURCE
) {
    private companion object {
        const val UNSET_SIZE = -1
        const val UNSET_COLOR = Integer.MAX_VALUE
        const val UNSET_FONT_RESOURCE = -1
    }

    fun apply(textView: TextView) {
        if (size != UNSET_SIZE) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size.toFloat())
        }
        if (color != UNSET_COLOR) {
            textView.setTextColor(color)
        }
        val fontTypeface = StyleUtils.getFont(textView.context, font)
        textView.setTypeface(fontTypeface, style)
    }

    class Builder(private val array: TypedArray) {
        private var size: Int = UNSET_SIZE
        private var color: Int = UNSET_COLOR
        private var style: Int = Typeface.NORMAL
        private var font: Int = -1

        fun size(@StyleableRes size: Int, @Px defValue: Int) = apply {
            this.size = array.getDimensionPixelSize(size, defValue)
        }

        fun color(@StyleableRes color: Int, @ColorInt defValue: Int) = apply {
            this.color = array.getColor(color, defValue)
        }

        fun style(@StyleableRes style: Int, defValue: Int) = apply {
            this.style = array.getInt(style, defValue)
        }

        fun font(@StyleableRes font: Int) =
            apply { this.font = array.getResourceId(font, -1) }

        fun build() = TextStyle(size, color, style, font)
    }
}