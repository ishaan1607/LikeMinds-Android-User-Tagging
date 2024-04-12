package com.likeminds.usertagging.model

import android.graphics.drawable.Drawable

class TagUser private constructor(
    val name: String,
    val imageUrl: String,
    val description: String?,
    val id: Int,
    val isLastItem: Boolean,
    val placeholder: Drawable?,
    val uuid: String
) {
    class Builder {
        private var name: String = ""
        private var imageUrl: String = ""
        private var description: String? = null
        private var id: Int = 0
        private var isLastItem: Boolean = false
        private var placeholder: Drawable? = null
        private var uuid: String = ""

        fun name(name: String) = apply { this.name = name }
        fun imageUrl(imageUrl: String) = apply { this.imageUrl = imageUrl }
        fun id(id: Int) = apply { this.id = id }
        fun isLastItem(isLastItem: Boolean) = apply { this.isLastItem = isLastItem }
        fun description(description: String?) = apply { this.description = description }
        fun placeholder(placeholder: Drawable?) = apply { this.placeholder = placeholder }
        fun uuid(uuid: String) = apply { this.uuid = uuid }

        fun build() = TagUser(
            name,
            imageUrl,
            description,
            id,
            isLastItem,
            placeholder,
            uuid
        )
    }

    fun toBuilder(): Builder {
        return Builder().name(name)
            .imageUrl(imageUrl)
            .id(id)
            .description(description)
            .isLastItem(isLastItem)
            .placeholder(placeholder)
            .uuid(uuid)
    }
}