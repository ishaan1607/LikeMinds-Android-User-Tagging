package com.likeminds.usertagging.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.core.content.ContextCompat
import com.likeminds.usertagging.R
import com.likeminds.usertagging.util.ImageUtils
import com.likeminds.usertagging.view.style.TextStyle

data class UserTaggingItemViewStyle(
    val userNameTextStyle: TextStyle,
    val descriptionTextStyle: TextStyle,
    val userImageCircular: Boolean,
    @ColorInt val userImageBorderColor: Int,
    @Px val userImageBorderWidth: Int,
    @ColorInt val dividerColor: Int,
    val userImagePlacement: Int
) {
    internal companion object {
        operator fun invoke(context: Context, attrs: AttributeSet?): UserTaggingItemViewStyle {
            context.obtainStyledAttributes(
                attrs,
                R.styleable.UserTaggingItemView,
                R.attr.userTaggingItemViewStyle,
                R.style.UserTaggingUI_UserTaggingItemView

            ).use { a ->
                val userNameTextStyle = TextStyle.Builder(a)
                    .size(
                        R.styleable.UserTaggingItemView_userTaggingItemUserNameTextSize,
                        context.resources.getDimensionPixelSize(R.dimen.user_tagging_ui_user_name_size)
                    )
                    .color(
                        R.styleable.UserTaggingItemView_userTaggingItemUserNameTextColor,
                        ContextCompat.getColor(context, R.color.black)
                    )
                    .style(
                        R.styleable.UserTaggingItemView_userTaggingItemUserNameTextStyle,
                        Typeface.BOLD
                    )
                    .font(
                        R.styleable.UserTaggingItemView_userTaggingItemUserNameTextFont
                    )
                    .build()
                val descriptionTextStyle = TextStyle.Builder(a)
                    .style(
                        R.styleable.UserTaggingItemView_userTaggingItemDescriptionTextStyle,
                        Typeface.NORMAL
                    )
                    .font(
                        R.styleable.UserTaggingItemView_userTaggingItemDescriptionTextFont
                    )
                    .size(
                        R.styleable.UserTaggingItemView_userTaggingItemDescriptionTextSize,
                        context.resources.getDimensionPixelSize(R.dimen.user_tagging_ui_description_size)
                    )
                    .color(
                        R.styleable.UserTaggingItemView_userTaggingItemDescriptionTextColor,
                        ContextCompat.getColor(context, R.color.brown_grey)
                    )
                    .build()

                val userImageCircular = a.getBoolean(
                    R.styleable.UserTaggingItemView_userTaggingItemUserImageCircular,
                    false
                )

                val userImageBorderColor = a.getColor(
                    R.styleable.UserTaggingItemView_userTaggingItemUserImageBorderColor,
                    ContextCompat.getColor(context, R.color.black)
                )

                val userImageBorderWidth = a.getDimensionPixelSize(
                    R.styleable.UserTaggingItemView_userTaggingItemUserImageBorderWidth,
                    ImageUtils.dpToPx(1)
                )

                val dividerColor = a.getColor(
                    R.styleable.UserTaggingItemView_userTaggingItemDividerColor,
                    ContextCompat.getColor(context, R.color.grey_v2)
                )

                val userImagePlacement = a.getInt(
                    R.styleable.UserTaggingItemView_userTaggingItemUserPlacement,
                    1
                )

                return UserTaggingItemViewStyle(
                    userNameTextStyle = userNameTextStyle,
                    descriptionTextStyle = descriptionTextStyle,
                    userImageCircular = userImageCircular,
                    userImageBorderColor = userImageBorderColor,
                    userImageBorderWidth = userImageBorderWidth,
                    dividerColor = dividerColor,
                    userImagePlacement = userImagePlacement
                )
            }
        }
    }
}