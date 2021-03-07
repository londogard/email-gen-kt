package com.londogard.emailgen

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
    val filename = "2021-03-09"

    val issue = Json.decodeFromString<Issue>(EmailHelper.getFullFileText("/issues/$filename.json"))
    val htmlFilename = "$filename-tipsrundan-${issue.number}.html"
    val html = StringBuilder()
        .appendHTML().html {
            head { unsafe { raw(EmailHelper.getEmailStyle()) } }
            body {
                createHeader(issue.number.toString(), issue.introduction, htmlFilename)
                createTipsBox()
                createSuperTips(issue.supertips)
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

fun BODY.createHeader(number: String, introduction: String, htmlFilename: String) {
    section {
        header {
            h1 {
                a("https://afry-south.github.io/", target = "blank") {
                    unsafe {
                        raw(
                            """
                        <svg width="24pt" height="24pt" version="1.0" viewBox="0 0 359 359" xmlns="http://www.w3.org/2000/svg">
                    <g transform="translate(0 359) scale(.1 -.1)">
                    <path d="m1620 3543c-266-28-556-134-785-288-399-267-661-661-757-1137-19-96-23-144-23-328 0-185 3-231 23-328 146-721 683-1259 1398-1403 137-27 401-37 541-20 766 94 1370 657 1525 1423 20 97 23 143 23 328s-3 231-23 328c-152 753-740 1310-1493 1417-94 13-336 18-429 8zm408-184c176-21 377-87 547-180 90-49 271-176 263-185-11-11-2523-701-2526-693-5 13 56 158 103 244 109 201 292 408 475 537 117 83 303 177 430 216 239 75 444 93 708 61zm784-603c-20-18-368-313-772-656-404-342-743-631-753-640-18-16-10-40 212-630 127-337 231-614 231-616 0-9-92-3-175 11-447 73-830 324-1077 705-181 278-272 622-251 942l6 98 424-216c273-139 429-213 441-209 9 3 43 29 75 58l57 52-432 222c-244 125-433 227-433 235 0 18 2455 699 2472 686 8-7 1-19-25-42zm162-114c8-14-1061-2327-1077-2330-8-2-17 3-19 10-163 425-399 1061-396 1068 4 11 1470 1259 1480 1260 4 0 9-4 12-8zm266-167c175-364 203-782 78-1175-152-475-539-863-1016-1019-100-33-218-58-228-49-3 4 241 540 542 1193 496 1077 549 1186 562 1169 7-11 35-64 62-119z"/>
                    </g>
                    </svg>
                    """.trimIndent()
                        )
                    }
                }
                +" Tipsrundan #$number"
            }

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
            p {
                b { +"\uD83D\uDDF3️ Tipsboxen" }
                br { }
                +"Email:"
                a(href = mailLink) { +" \uD83D\uDCE7" }
                +" - GitHub: "
                a(href = "https://github.com/afry-south/afry-south.github.io/issues/1", target = "_blank") {
                    img(src = "https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png") {
                        width = "24px"
                    }
                }
                br { }
                +"All tips and recommendations are appreciated! \uD83D\uDE4F"
            }
        }
    }
}

fun BODY.createSuperTips(supertips: Item) {
    section {
        aside {
            p {
                b { +"\uD83D\uDCA1 Supertipset - ${supertips.title}" }
            }
            unsafe { raw(EmailHelper.markdownToHtml(supertips.description)) }
        }
    }
}

fun BODY.createBody(issue: Issue): Unit = main {
    hr { }
    section {
        header { h2 { +"Godisboxen \uD83C\uDF6D" } }
        issue.godisboxen.forEach { createCard(it) }
    }
}

fun BODY.createFooter() = footer {
    hr { }
    section {
        p {
            b { +"Thank you for this time see you in two weeks" }
            br { }
            +"- Hampus Londögård @ IT South"
        }
    }
}

fun SECTION.createCard(item: Item): Unit = aside {
    p {
        b { +item.getEmojifiedTitle() }
    }
    unsafe {
        raw(EmailHelper.markdownToHtml(item.description))
    }
    item.link?.let { link -> a(link, target = "blank") { +"Read more" } }
}