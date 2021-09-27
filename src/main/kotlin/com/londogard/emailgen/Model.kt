package com.londogard.emailgen

import kotlinx.serialization.Serializable

@Serializable
data class Issue(
        val number: Int,
        val introduction: String,
        val supertips: Item? = null,
        val godisboxen: List<Item>
)

@Serializable
data class Item(
        val title: String,
        val description: String, // TODO markdown support
        val link: String? = null,
        val category: Section = Section.RANDOM,  // TODO use this in title
        val afry: Boolean = false   // TODO render AFRY logo for this
) {
    fun getEmojifiedTitle(): String = if (afry) "[AFRY] ${category.emoji} $title" else "${category.emoji} $title"
}

