package com.londogard

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
    val softValues: List<Item>
)

@Serializable
data class Item(
    val title: String,
    val description: String,
    val link: String?
)

data class PinnedItem(val title: String, val url: String)