package com.likeminds.usertagging.view.adapter

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.likeminds.usertagging.databinding.ItemTagUserBinding
import com.likeminds.usertagging.model.TagUser

internal class TagUserViewHolder(
    val binding: ItemTagUserBinding,
    val darkMode: Boolean,
    private val memberAdapterClickListener: TagUserAdapterClickListener
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            val member = binding.member ?: return@setOnClickListener
            memberAdapterClickListener.onMemberTagged(member)
        }
    }

    @JvmSynthetic
    internal fun bind(userAndGroup: TagUser) {
        binding.member = userAndGroup
        binding.hideBottomLine = userAndGroup.isLastItem && !darkMode
        binding.darkMode = darkMode

        //set description and hide in case of description is empty
        binding.tvDescription.apply {
            isVisible = userAndGroup.description.isNotEmpty()
            text = userAndGroup.description
        }

        Glide.with(binding.ivMemberImage)
            .load(userAndGroup.imageUrl)
            .placeholder(userAndGroup.placeholder)
            .error(userAndGroup.placeholder)
            .into(binding.ivMemberImage)
        binding.executePendingBindings()
    }
}