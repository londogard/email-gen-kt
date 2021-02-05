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
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

fun main() = runBlocking {
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

    val filename = "2021-01-26"

    val issue = Json.decodeFromString<Issue>(EmailHelper.getFullFileText("/issues/$filename.json"))
    val htmlFilename = "$filename-tipsrundan-${issue.number}.html"
    val html = StringBuilder()
        .appendHTML().html {
            head { unsafe { raw(EmailHelper.getEmailStyle()) } }
            body {
                createHeader(issue.number.toString(), issue.introduction, pinned, htmlFilename)
                createTipsBox()
                createBody(issue)
                createFooter()
            }
        }.toString()

    val inlinedHtml = HttpClient(CIO).use { client ->
        client.submitForm<String>(
            url = "https://templates.mailchimp.com/services/inline-css/",
            formParameters = parametersOf("html", html)
        )
    }

    val fullHtml = """
---
title: "Tipsrundan ${issue.number}"
excerpt: "${issue.introduction}"
---

$inlinedHtml
    """.trimIndent()

    File(htmlFilename).writeText(fullHtml)
}


fun BODY.createHeader(number: String, introduction: String, pinned: List<PinnedItem>, htmlFilename: String) {
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
            p {
                +introduction
                br {}
                +"As always, read the pretty version "
                a(href = "https://afry-south.github.io/tipsrundan/${htmlFilename.removeSuffix(".html")}") { +"here" }
            }
        }
    }
}

fun BODY.createTipsBox() {
    val mailLink = "mailto:it.south.competence@afconsult.com?subject=Tipsbox&body=Title%0D%0ASummary%0D%0ALink"

    section {
        aside {
            b { +"\uD83D\uDDF3️ Tipsboxen" }
            br {  }
            p {
                +"Email:"
                a(href = mailLink) { +" \uD83D\uDCE7" }
                +" - GitHub: "
                a(href = "https://github.com/afry-south/afry-south.github.io/issues/1", target="_blank") {
                    img(src="https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png"){ width = "24px" }
                }
                br {  }
                +"All tips and recommendations are appreciated! \uD83D\uDE4F"
            }
        }
    }
}

fun BODY.createBody(issue: Issue): Unit = main {
    listOf(
        issue.regional to REGIONAL,
        issue.conferenceAndLearning to CONFERENCESANDLEARNING,
        issue.softValues to SOFTVALUES,
        issue.backendAndBigData to BACKEND,
        issue.frontendAndMobile to FRONTEND,
        issue.testing to TESTING,
        issue.machineLearning to MACHINELEARNING,
        issue.videosAndPodcasts to VIDEOPODCASTS,
        issue.random to RANDOM
    )
        .filterNot { (items, _) -> items.isEmpty() }
        .forEach { (items, type) -> createSection(items, type) }
}

fun BODY.createFooter() = footer {
    hr { }
    section {
        p {
            b { +"Thank you for this time see you in two weeks" }
            br { }
            +"Hampus"
        }
    }
}

fun MAIN.createSection(items: List<Item>, title: String) {
    hr { }
    section {
        header { h2 { +title } }
        items.forEach(::createCard)
    }
}

fun SECTION.createCard(item: Item): Unit = aside {
    p {
        b { +item.title }
        br
        p { +item.description }
    }
    item.link?.let { link -> a(link, target = "blank") { +"Read more" } }
}