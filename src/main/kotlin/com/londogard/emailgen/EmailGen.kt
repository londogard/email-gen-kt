package com.londogard.emailgen

import com.londogard.emailgen.EmailHelper.buSlackArchive
import com.londogard.emailgen.SectionTitles.Companion.BACKEND
import com.londogard.emailgen.SectionTitles.Companion.CONFERENCESANDLEARNING
import com.londogard.emailgen.SectionTitles.Companion.FRONTEND
import com.londogard.emailgen.SectionTitles.Companion.MACHINELEARNING
import com.londogard.emailgen.SectionTitles.Companion.RANDOM
import com.londogard.emailgen.SectionTitles.Companion.REGIONAL
import com.londogard.emailgen.SectionTitles.Companion.SOFTVALUES
import com.londogard.emailgen.SectionTitles.Companion.TESTING
import com.londogard.emailgen.SectionTitles.Companion.VIDEOPODCASTS
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse
import java.io.File

// https://emailframe.work/
fun main() {
    val pinned = listOf(
        PinnedItem(
            title = "Recommended Online Courses",
            url = "$buSlackArchive/CPK80KX0W/p1576416425001400"
        ),
        PinnedItem(
            title = "Competence Groups",
            url = "$buSlackArchive/CPK80KX0W/p1571639001000400"
        )
    )
    val filename = "2020-09-15"
    //val json = Json(JsonConfiguration.Stable)

    val issue = Json.decodeFromString(
        Issue.serializer(),
        EmailHelper.getFullFileText("/issues/$filename.json")
    )

    val html = StringBuilder().appendHTML().html {
        head { unsafe { raw(EmailHelper.getEmailStyle()) } }
        body {
            createHeader(issue.number.toString(), issue.introduction, pinned)
            createBody(issue)
            createFooter()
        }
    }

    // Add this if you want to put the content in your clipboard (ctrl+v to paste the raw html)
    // Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(html.toString()), null)

    File("$filename.html").writeText(html.toString())
}


fun BODY.createHeader(number: String, introduction: String, pinned: List<PinnedItem>) {
    header {
        nav {
            a("https://afry-south.github.io/", target = "blank") {
                img {
                    height = "70"
                    src = "https://afry.com/sites/default/files/2019-11/AFRY_Logotyp_Liggande_PNG.png"
                }
            }
            ul {
                pinned.forEach { pinnedItem ->
                    li { a(pinnedItem.url, target = "blank") { +pinnedItem.title } }
                }
            }
        }
    }
    section {
        header {
            h1 { +"Tipsrundan #$number" }
            p { +introduction }
            p {
                + "As always, read the pretty version "
                a(href="https://afry-south.github.io/tipsrundan/Tipsrundan_$number.html") { +"here" }
            }
        }
    }
}

fun BODY.createBody(issue: Issue): Unit = main {
    createSection(issue.regional, REGIONAL)
    createSection(issue.conferenceAndLearning, CONFERENCESANDLEARNING)
    createSection(issue.softValues, SOFTVALUES)
    createSection(issue.backendAndBigData, BACKEND)
    createSection(issue.frontendAndMobile, FRONTEND)
    createSection(issue.testing, TESTING)
    createSection(issue.machineLearning, MACHINELEARNING)
    createSection(issue.videosAndPodcasts, VIDEOPODCASTS)
    createSection(issue.random, RANDOM)
}

fun BODY.createFooter() = footer {
    hr { }
    section {
        p {
            b { +"Thank you for this time see you in two weeks" }
            br { }
            +"Hampus & Hassan"
        }
    }
}

fun MAIN.createSection(items: List<Item>, title: String): Unit = when (items) {
    emptyList<Item>() -> Unit
    else -> {
        hr { }
        section {
            header { h2 { +title } }
            items.forEach(::createCard)
        }
    }
}

fun SECTION.createCard(item: Item): Unit = aside {
    p {
        b { +item.title }
        br
        p { +item.description }
    }
    a(item.link, target = "blank") { +"Read more" }
}