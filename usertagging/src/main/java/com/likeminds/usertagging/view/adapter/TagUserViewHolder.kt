package com.likeminds.usertagging.view.adapter

import android.content.res.ColorStateList
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.likeminds.usertagging.R
import com.likeminds.usertagging.databinding.ItemTagUserBinding
import com.likeminds.usertagging.model.TagUser
import com.likeminds.usertagging.view.UserTaggingItemViewStyle

internal class TagUserViewHolder(
    private val binding: ItemTagUserBinding,
    private val style: UserTaggingItemViewStyle,
    private val memberAdapterClickListener: TagUserAdapterClickListener,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.apply {
            applyStyle(style)
            root.setOnClickListener {
                val user = binding.tagUser ?: return@setOnClickListener
                memberAdapterClickListener.onUserTagged(user)
            }
        }
    }

    @JvmSynthetic
    internal fun bind(user: TagUser) {
        binding.tagUser = user
        binding.hideBottomLine = user.isLastItem

        //set description and hide in case of description is empty
        binding.tvDescription.apply {
            isVisible = !user.description.isNullOrEmpty()
            text = user.description
        }

        Glide.with(binding.ivMemberImage)
            .load(user.imageUrl)
            .placeholder(user.placeholder)
            .error(user.placeholder)
            .into(binding.ivMemberImage)
        binding.executePendingBindings()
    }

    private fun ItemTagUserBinding.applyStyle(style: UserTaggingItemViewStyle) {
        style.userNameTextStyle.apply(tvMemberName)
        style.descriptionTextStyle.apply(tvDescription)

        ivMemberImage.apply {
            strokeColor = ColorStateList.valueOf(style.userImageBorderColor)
            strokeWidth = style.userImageBorderWidth.toFloat()
        }
        divider.setBackgroundColor(style.dividerColor)

        ivMemberImage.shapeAppearanceModel = if (style.userImageCircular) {
            val radius =
                this.root.context.resources.getDimension(R.dimen.user_tagging_ui_circular_image)
            ivMemberImage.shapeAppearanceModel.toBuilder()
                .setAllCornerSizes(radius)
                .build()
        } else {
            val radius =
                this.root.context.resources.getDimension(R.dimen.user_tagging_ui_square_image)
            ivMemberImage.shapeAppearanceModel.toBuilder()
                .setAllCornerSizes(radius)
                .build()
        }
    }
}