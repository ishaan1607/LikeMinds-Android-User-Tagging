package com.likeminds.usertagging.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.likeminds.usertagging.databinding.ItemTagUserBinding
import com.likeminds.usertagging.model.TagUser
import com.likeminds.usertagging.view.UserTaggingItemViewStyle

internal class TagUserAdapter(
    private val style: UserTaggingItemViewStyle,
    private val memberAdapterClickListener: TagUserAdapterClickListener
) : RecyclerView.Adapter<TagUserViewHolder>() {

    private val tagUsers = ArrayList<TagUser>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagUserViewHolder {
        val binding = ItemTagUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TagUserViewHolder(binding, style, memberAdapterClickListener)
    }

    override fun onBindViewHolder(holder: TagUserViewHolder, position: Int) {
        holder.bind(tagUsers[position])
    }

    override fun getItemCount() = tagUsers.size

    /**
     * Updates the member list in the recyclerview adapter
     */
    @SuppressLint("NotifyDataSetChanged")
    @JvmSynthetic
    internal fun setMembers(users: List<TagUser>) {
        this.tagUsers.clear()
        this.tagUsers.addAll(users)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    @JvmSynthetic
    internal fun allMembers(users: List<TagUser>) {
        this.tagUsers.addAll(users)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    @JvmSynthetic
    internal fun clear() {
        this.tagUsers.clear()
        notifyDataSetChanged()
    }
}