package com.likeminds.usertagging.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.likeminds.usertagging.databinding.ItemTagUserBinding
import com.likeminds.usertagging.model.TagUser

internal class TagUserAdapter(
    private val darkMode: Boolean,
    private val memberAdapterClickListener: TagUserAdapterClickListener
) : RecyclerView.Adapter<TagUserViewHolder>() {

    private val members = ArrayList<TagUser>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagUserViewHolder {
        val binding = ItemTagUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TagUserViewHolder(binding, darkMode, memberAdapterClickListener)
    }

    override fun onBindViewHolder(holder: TagUserViewHolder, position: Int) {
        holder.bind(members[position])
    }

    override fun getItemCount() = members.size

    /**
     * Updates the member list in the recyclerview adapter
     */
    @SuppressLint("NotifyDataSetChanged")
    @JvmSynthetic
    internal fun setMembers(users: List<TagUser>) {
        this.members.clear()
        this.members.addAll(users)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    @JvmSynthetic
    internal fun allMembers(users: List<TagUser>) {
        this.members.addAll(users)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    @JvmSynthetic
    internal fun clear() {
        this.members.clear()
        notifyDataSetChanged()
    }
}