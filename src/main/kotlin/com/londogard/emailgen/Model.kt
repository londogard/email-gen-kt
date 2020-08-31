package com.londogard.emailgen

import kotlinx.serialization.Serializable

@Serializable
data class Issue(
    val number: Int,
    val introduction: String,
    val regional: List<Item>,
    val videosAndPodcasts: List<Item>,
    val conferenceAndLearning: List<Item>,
    val frontendAndMobile: List<Item>,
    val backendAndBigData: List<Item>,
    val testing: List<Item>,
    val machineLearning: List<Item>,
    val softValues: List<Item>,
    val random: List<Item>
)

@Serializable
data class Item(
    val title: String,
    val description: String,
    val link: String? = null,
    val afry: Boolean? = null
)

@Serializable
data class InlineHtml(val html: String)

data class PinnedItem(val title: String, val url: String)