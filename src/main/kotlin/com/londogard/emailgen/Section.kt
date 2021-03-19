package com.londogard.emailgen

import kotlinx.serialization.Serializable

@Serializable
enum class Section(val emoji: String) {
    REGIONAL(""),
    VIDEOPODCASTS("\uD83D\uDCBB\uD83C\uDFA7"),
    CONFERENCESANDLEARNING("\uD83D\uDCDA"),
    SOFTVALUES("\uD83D\uDC65"),
    FRONTEND("\uD83D\uDCF1"),
    BACKEND("\uD83C\uDF92"),
    TESTING("\uD83E\uDDEA"),
    MACHINELEARNING("\uD83D\uDCC8"),
    RANDOM("\uD83D\uDD00")
}