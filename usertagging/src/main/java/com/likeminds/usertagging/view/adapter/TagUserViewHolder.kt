package com.likeminds.usertagging.view.adapter

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.likeminds.usertagging.databinding.ItemTagUserBinding
import com.likeminds.usertagging.model.TagUser

internal class TagUserViewHolder(
    private val binding: ItemTagUserBinding,
    private val memberAdapterClickListener: TagUserAdapterClickListener
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.root.setOnClickListener {
            val user = binding.tagUser ?: return@setOnClickListener
            memberAdapterClickListener.onUserTagged(user)
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
}