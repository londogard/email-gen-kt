package com.londogard.emailgen

import io.ktor.client.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

fun main() = runBlocking {
    val filenames = File("~/git/email-gen-kt/src/main/resources/issues")
        .listFiles()
        .map { it.nameWithoutExtension }
        .forEach { f -> println(f);generateMd(f) }
    //val filename = "2021-09-19"
    //generateMd(filename)
    // generateHtml(filename)
}

fun generateMd(filename: String) {
    val issue = Json.decodeFromString<Issue>(EmailHelper.getFullFileText("/issues/$filename.json"))
    val mdFilename = "$filename-tipsrundan-${issue.number}.md"
    val md = """
---
title: "Tipsrundan ${issue.number}"
description: "${issue.introduction}"
slug: ${issue.number}
authors: [hlondogard]
---
_👋 Welcome to [Tipsrundan](https://afry-south.github.io/tipsrundan/$filename-tipsrundan-${issue.number}/)! A biweekly newsletter by AFRY IT South with ❤️_  
_${issue.introduction}_
<!--truncate-->

[Tipslådan 🗳](mailto:hampus.londogard@afry.com?subject=Tips)    

---
${
        if (issue.supertips != null) {
            """
## Supertipset 💡
###         ${issue.supertips.title}

${issue.supertips.description}

---
    """.trimIndent()
        } else ""
    }



## Godisboxen 🍭
        
${
        issue
            .godisboxen
            .map(Item::toItemMd)
            .joinToString("\n\n")
    }   

---

**Thank you for this time see you in two weeks**   
- Hampus Londögård @ AFRY IT South
    """.trimIndent()

    File(mdFilename).writeText(md)
}

fun Item.toItemMd(): String {
    return """
### ${this.getEmojifiedTitle()}

${this.description}

${this.link?.toLink() ?: ""}
    """.trimIndent()
}

fun String.toLink(): String {
    val simplifiedLink = this.replace("https?://(www.)?".toRegex(), "").split("/").first()

    return "[$simplifiedLink↗]($this)"
}

fun generateHtml(filename: String) = runBlocking {
    val issue = Json.decodeFromString<Issue>(EmailHelper.getFullFileText("/issues/$filename.json"))
    val htmlFilename = "$filename-tipsrundan-${issue.number}.html"
    val html = StringBuilder()
        .appendHTML().html {
            head { unsafe { raw(EmailHelper.getEmailStyle()) } }
            body {
                createHeader(issue.number.toString(), filename)
                div("section") {
                    div("aside") {
                        p {
                            style = "text-align: center;width:75%;margin: 0.75rem auto;"
                            +"\uD83D\uDC4B"
                            i {
                                +" Welcome to Tipsrundan! "
                                br {}
                                +issue.introduction
                            }
                        }
                        a(href = "https://afry-south.github.io/tipsrundan/${htmlFilename.removeSuffix(".html")}") {
                            style = "margin:0.5rem auto;"
                            button {
                                +"Take me to pretty version!"
                            }
                        }
                    }
                }


                hr { }
                createSuperTips(issue.supertips!!)
                createBody(issue)
                createFooter()
            }
        }.toString()

    val inlinedHtml = HttpClient().use { client ->
        client.submitForm<String>(
            url = "https://templates.mailchimp.com/services/inline-css/",
            formParameters = parametersOf("html", html)
        )
    }

    val fullHtml = """
---
layout: splash
title: "Tipsrundan ${issue.number}"
excerpt: "${issue.introduction}"
---

<table border="0" cellspacing="0" width="100%">
<tr>
<td></td>
<td width="800">$inlinedHtml</td>
<td></td>
</tr>
</table>
    """.trimIndent()

    File(htmlFilename).writeText(fullHtml)
}

fun BODY.createHeader(issue: String, date: String) {
    val mailLink = "mailto:it.south.competence@afconsult.com?subject=Tipsbox&body=Title%0D%0ASummary%0D%0ALink"
    div("section") {
        div("header") {
            p {
                +"$date - Issue #$issue - "
                a(href = "https://afry-south.github.io/") { +"Blog ✍️" }
            }
            h1 {
                a("https://afry-south.github.io/", target = "blank") {
                    img(src = "https://raw.githubusercontent.com/afry-south/afry-south.github.io/master/afry.png")
                }
                +" Tipsrundan"
            }
            p {
                +"Your biweekly newsletter of 'Tips' from "
                b { +"IT South@AFRY" }
                +" with ❤️ "
                br { }
                small {
                    +"(\uD83D\uDDF3️ Tipsboxen ("
                    a(href = mailLink) { +"Email" } // 📧
                    +", "
                    a(href = "https://github.com/afry-south/afry-south.github.io/issues/1", target = "_blank") {
                        +"GitHub"
                        //img(src = "https://github.githubassets.com/images/modules/logos_page/Octocat.png") {
                        //    width = "24px"
                        //}
                    }
                    +" & "
                    a(href = "https://buitsouth.slack.com/archives/CPK80KX0W", target = "_blank") {
                        +"Slack"
                        //img(src = "https://assets.brandfolder.com/pl546j-7le8zk-afym5u/v/3033396/original/Slack_Mark_Web.png") {
                        //    width = "24px"
                        //}
                    }
                    +") - All tips are appreciated! \uD83D\uDE4F)"
                }
            }
        }
    }
}

fun BODY.createSuperTips(supertips: Item) {
    h1 { +"Supertipset \uD83D\uDCA1" }
    div("section") {
        div("aside") {
            h3 { +supertips.title }
            unsafe { raw(EmailHelper.markdownToHtml(supertips.description)) }
        }
    }
}

fun BODY.createBody(issue: Issue): Unit = main {
    hr { }
    h1 { +"Godisboxen \uD83C\uDF6D" }
    div("section") {
        issue.godisboxen.forEach { createCard(it) }
    }
}

fun BODY.createFooter() = div("footer") {
    div("section") {
        p {
            b { +"Thank you for this time see you in two weeks" }
            br { }
            +"- Hampus Londögård @ IT South"
        }
    }
}

fun DIV.createCard(item: Item): Unit = div("aside") {
    h3 { +item.getEmojifiedTitle() }
    unsafe { raw(EmailHelper.markdownToHtml(item.description)) }

    item.link?.let { link ->
        val simplifiedLink = link.replace("https?://(www.)?".toRegex(), "").split("/").first()
        a(link, target = "blank") {
            style = "width:100%;"
            small { +"$simplifiedLink↗" }
        }
    }
    hr { style = "width:10%;" }
}