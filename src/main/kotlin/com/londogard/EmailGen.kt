package com.londogard

import com.londogard.Colors.Companion.REGIONAL
import com.londogard.EmailGen.buSlackArchive
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import kotlinx.serialization.parse
import java.io.File
import java.lang.StringBuilder

object EmailGen {
    fun emailStyle(number: String): String = """
    <link rel="stylesheet" href="https://andybrewer.github.io/mvp/mvp.css">

    <meta charset="utf-8">
    <meta name="description" content="My description">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Tipsrundan $number</title>"""

    fun getFullFileText(path: String): String =
        javaClass.getResourceAsStream(path).bufferedReader().readText()

    const val buSlackArchive = "https://buitsouth.slack.com/archives"

}

fun main() {
    val pinned = listOf(
        PinnedItem("Recommended Online Courses", "$buSlackArchive/CPK80KX0W/p1576416425001400"),
        PinnedItem("Competence Groups", "$buSlackArchive/CPK80KX0W/p1571639001000400")
    )
    val filename = "2020-03-26.json"
    val json = Json(JsonConfiguration.Stable)
    val issue = json.parse(Issue.serializer(), EmailGen.getFullFileText("/issues/$filename"))

    val b = StringBuilder().appendHTML().div {
        unsafe { raw(EmailGen.emailStyle(issue.number.toString())) }
        createHeader(issue.number.toString(), issue.introduction, pinned)
        createBody(issue)
    }
    File("a.html").writeText(b.toString())
}

/** var html = createHeader(result.number.toString(), result.date)
html += createTitle(result.title)
result.announcements?.let { html += createAnnouncements(result.announcements) }
result.articles?.let { html += createArticles(result.articles) }
result.sponsored?.let { html += createSponsored(result.sponsored) }
result.android?.let { html += createAndroid(result.android) }
result.kotlinMultiplatformArticles?.let { html += createMultiplatform(result.kotlinMultiplatformArticles) }
result.videos?.let { html += createVideos(result.videos) }
result.jobs?.let { html += createJobs(result.jobs) }
result.podcast?.let { html += createPodcast(result.podcast) }
result.conferences?.let { html += createConferences(result.conferences) }
result.libraries?.let { html += createLibraries(result.libraries) }

html += createFooter()

//val clipboard = Toolkit.getDefaultToolkit().systemClipboard
//clipboard.setContents(StringSelection(html), null)

print(html)
}**/

// https://andybrewer.github.io/mvp/mvp.html

fun DIV.createHeader(number: String, introduction: String, pinned: List<PinnedItem>) {
    header {
        nav {
            a("https://afry-south.github.io/", target="blank") {
                img {
                    height = "70"
                    src = "https://afry.com/sites/default/files/2019-11/AFRY_Logotyp_Liggande_PNG.png"
                }
            }
            ul {
                pinned.forEach { pinnedItem ->
                    li { a(pinnedItem.url, target="blank") { +pinnedItem.title } }
                }
            }
        }
    }
    section {
        header {
            h2 { +"Tipsrundan #$number" }
            p { +introduction }
        }
    }
}
fun DIV.createBody(issue: Issue): Unit = main {
    createSection(issue.regional, "Regional", REGIONAL)

}

fun MAIN.createSection(items: List<Item>, title: String, colors: String): Unit = when (items) {
    emptyList<Item>() -> Unit
    else -> section {
        h3 { +title }
        items.forEach {
            // TODO createCard
        }
    }
}



/**
fun createTitle(title: String): String {
return "<p><span style=\"font-size:16px\"><span style=\"font-family:helvetica neue\">$title</span></span></p></br>\n"
}

fun createAnnouncements(articles: List<Item>): String {
var html = "<div style=\"overflow: hidden;\">\n" +
"<div style=\"\n" +
"    width:30%;\n" +
"\n" +
"    float:left\">\n" +
"<p style=\"text-align: center;\"><img data-file-id=\"2573165\" height=\"45\" src=\"https://gallery.mailchimp.com/f39692e245b94f7fb693b6d82/images/b61142be-473c-436a-84ad-8338e13e31da.png\" style=\"border: 0px  ; width: 45px; height: 45px; margin: 0px;\" width=\"45\" /><br />\n" +
"<strong><span style=\"font-size:11px\"><span style=\"font-family:helvetica neue,helvetica,arial,verdana,sans-serif\"><span style=\"color:#FFFFFF\"><span style=\"background-color:#7874b4; padding:5px 5px 5px 5px\">Announcements</span></span></span></span></strong></p>\n" +
"</div>\n"
html += "<div style=\"  \n" +
"    width:70%;\n" +
"    float:left;\n" +
"    \">\n"

for (article in articles) {
html += "<a href=\"" + article + "\" style=\"text-decoration:none\" target=\"_blank\"><span style=\"font-size:16px\"><span style=\"font-family:helvetica neue,helvetica,arial,verdana,sans-serif\"><strong><span style=\"color:#7874b4\">" + article.title + "</span></strong></span></span></a><br />\n" +
"<span style=\"font-size:15px\">" + article.description + "</span><br />"
var url = URL(article.link)
html += "<a href=\"" + url.host + "\" style=\"text-decoration:none\" target=\"_blank\"><strong><span style=\"font-size:15px; color:#7874b4\">" + url.host + "</span></strong></a><br />\n" +
"<br />"
}
html = html.dropLast(6)
html += "</div></div>\n"

return html
}

fun createArticles(articles: List<Item>): String {
var html = "\n" +
"<div style=\"overflow: hidden;\">\n" +
"<div style=\"\n" +
"    width:30%;\n" +
"    float:left\">\n" +
"<p style=\"text-align: center;\"><br />\n" +
"<img data-file-id=\"2573169\" height=\"64\" src=\"https://gallery.mailchimp.com/f39692e245b94f7fb693b6d82/images/aecc15c5-3b55-4f56-b171-6c933eabb173.png\" style=\"border: 0px initial ; width: 65px; height: 64px; margin: 0px;\" width=\"65\" /><br />\n" +
"<strong><span style=\"font-size:11px\"><span style=\"font-family:helvetica neue,helvetica,arial,verdana,sans-serif\"><span style=\"color:#FFFFFF\"><span style=\"background-color:" + Colors.ARTICLES + "; padding:5px 5px 5px 5px\">Articles</span></span></span></span></strong></p>\n" +
"</div>\n"
html += "<div style=\"  \n" +
"    width:70%;\n" +
"    float:left;\n" +
"    \"><br />\n"

for (article in articles) {
html += "<a href=\"" + article + "\" style=\"text-decoration:none\" target=\"_blank\"><span style=\"font-size:16px\"><span style=\"font-family:helvetica neue,helvetica,arial,verdana,sans-serif\"><strong><span style=\"color:" + Colors.ARTICLES + "\">" + article.title + "</span></strong></span></span></a><br />\n" +
"<span style=\"font-size:15px\">" + article.description + "</span><br />"
var url = URL(article.link)
html += "<a href=\"" + url.host + "\" style=\"text-decoration:none\" target=\"_blank\"><strong><span style=\"font-size:15px; color:" + Colors.ARTICLES + "\">" + url.host + "</span></strong></a><br />\n" +
"<br />"
}
html = html.dropLast(6)
html += "</div></div><br />\n"

return html
}

fun createAndroid(articles: List<Item>): String {
var html = "<div style=\"overflow: hidden;\">\n" +
"<div style=\"\n" +
"    width:30%;\n" +
"    float:left\">\n" +
"<p style=\"text-align: center;\"><br />\n" +
"<img data-file-id=\"2573205\" height=\"65\" src=\"https://gallery.mailchimp.com/f39692e245b94f7fb693b6d82/images/57e35131-d16d-4d0c-a6a6-ff0c005d0c16.png\" style=\"border: 0px initial ; width: 65px; height: 65px; margin: 0px;\" width=\"65\" /><br />\n" +
"<strong><span style=\"font-size:11px\"><span style=\"font-family:helvetica neue,helvetica,arial,verdana,sans-serif\"><span style=\"color:#FFFFFF\"><span style=\"background-color:#79c5b4; padding:5px 5px 5px 5px\">Android</span></span></span></span></strong></p>\n" +
"</div>"
html += "<div style=\"  \n" +
"    width:70%;\n" +
"    float:left;\n" +
"    \"><br />\n"
for (article in articles) {
html += "<a href=\"" + article + "\" style=\"text-decoration:none\" target=\"_blank\"><span style=\"font-size:16px\"><span style=\"font-family:helvetica neue,helvetica,arial,verdana,sans-serif\"><strong><span style=\"color:" + Colors.ANDROID + "\">" + article.title + "</span></strong></span></span></a><br />\n" +
"<span style=\"font-size:15px\">" + article.description + "</span><br />"
val url = URL(article.link)
html += "<a href=\"" + url.host + "\" style=\"text-decoration:none\" target=\"_blank\"><strong><span style=\"font-size:15px; color:" + Colors.ANDROID + "\">" + url.host + "</span></strong></a><br />\n" +
"<br />"
}
html = html.dropLast(6)
html += "</div></div>\n"
return html
}

fun createSponsored(sponsored: Item): String {
var html = "<div style=\"overflow: hidden;\">\n" +
"<div style=\"background-color:#f0f0f0;\n" +
"    width:30%;\n" +
"    float:left\">\n" +
"<p style=\"text-align: center;\"><img data-file-id=\"2573201\" height=\"65\" src=\"https://gallery.mailchimp.com/f39692e245b94f7fb693b6d82/images/57e35131-d16d-4d0c-a6a6-ff0c005d0c16.png\" style=\"border: 0px  ; width: 79px; height: 40px; margin: 0px;\" width=\"79\" /><br />\n" +
"<strong><span style=\"font-size:11px\"><span style=\"font-family:helvetica neue,helvetica,arial,verdana,sans-serif\"><span style=\"color:#FFFFFF\"><span style=\"background-color:#b3a34f; padding:5px 5px 5px 5px\">Sponsored</span></span></span></span></strong></p>\n" +
"</div>"
val url = URL(sponsored.link)
html += "<div style=\"  background-color:#f0f0f0;\n" +
"    width:66%;\n" +
"    float:left; padding:5px 5px 5px 10px\n" +
"    \"><a href=\"" + sponsored.shortUrl + "\" style=\"text-decoration:none\" target=\"_blank\"><strong><span style=\"font-size:16px\"><span style=\"font-family:helvetica neue,helvetica,arial,verdana,sans-serif\"><span style=\"color:" + Colors.SPONSORED + "\">" + sponsored.title + "</span></span></span></strong></a><br />\n" +
"<span style=\"font-size:15px\">" + sponsored.description + "</span><br />" +
"<a href=\"" + url.host + "\" style=\"text-decoration:none\" target=\"_blank\"><strong><span style=\"font-size:15px; color:" + Colors.SPONSORED + "\">getstream.io</span></strong></a></div>\n" +
"</div>\n"
return html
}

fun createMultiplatform(articles: List<Item>): String {
var html = "<div style=\"overflow: hidden;\">\n" +
"<div style=\"\n" +
"    width:30%;\n" +
"    float:left\">\n" +
"<p style=\"text-align: center;\"><br />\n" +
"<img data-file-id=\"2573205\" height=\"45\" src=\"https://gallery.mailchimp.com/f39692e245b94f7fb693b6d82/images/d6dd1767-fc87-4f29-8899-ab4caae6903d.png\" style=\"border: 0px initial ; width: 45px; height: 45px; margin: 0px;\" width=\"45\" /><br />\n" +
"<strong><span style=\"font-size:11px\"><span style=\"font-family:helvetica neue,helvetica,arial,verdana,sans-serif\"><span style=\"color:#FFFFFF\"><span style=\"background-color:" + Colors.KOTLIN_MULTIPLATFORM + "; padding:5px 5px 5px 5px\">Multiplatform</span></span></span></span></strong></p>\n" +
"</div>"
html += "<div style=\"  \n" +
"    width:70%;\n" +
"    float:left;\n" +
"    \"><br />\n"
for (article in articles) {
html += "<a href=\"" + article + "\" style=\"text-decoration:none\" target=\"_blank\"><span style=\"font-size:16px\"><span style=\"font-family:helvetica neue,helvetica,arial,verdana,sans-serif\"><strong><span style=\"color:" + Colors.KOTLIN_MULTIPLATFORM + "\">" + article.title + "</span></strong></span></span></a><br />\n" +
"<span style=\"font-size:15px\">" + article.description + "</span><br />"
val url = URL(article.link)
html += "<a href=\"" + url.host + "\" style=\"text-decoration:none\" target=\"_blank\"><strong><span style=\"font-size:15px; color:" + Colors.KOTLIN_MULTIPLATFORM + "\">" + url.host + "</span></strong></a><br />\n" +
"<br />"
}
html = html.dropLast(6)
html += "</div></div><br />\n"
return html
}

fun createJobs(articles: List<Item>): String {
var html = "<div style=\"overflow: hidden;\">\n" +
"<div style=\"background-color:#f0f0f0;\n" +
"    width:30%;\n" +
"    float:left\">\n" +
"<p style=\"text-align: center;\"><img data-file-id=\"2573181\" height=\"65\" src=\"https://gallery.mailchimp.com/f39692e245b94f7fb693b6d82/images/b3899433-4991-436c-b3ac-f4f309129ed3.jpg\" style=\"border: 0px  ; width: 65px; height: 65px; margin: 0px;\" width=\"65\" /><br />\n" +
"<strong><span style=\"font-size:11px\"><span style=\"font-family:helvetica neue,helvetica,arial,verdana,sans-serif\"><span style=\"color:#FFFFFF\"><span style=\"background-color:" + Colors.JOBS + "; padding:5px 5px 5px 5px\">Jobs</span></span></span></span></strong></p>\n" +
"</div>"
for (article in articles) {
html += "<div style=\"background-color:#f0f0f0;\n" +
"    width:66%;\n" +
"     float:left; padding:5px 5px 5px 10px\n" +
"    \">\n" +
"<a href=\"" + article + "\" style=\"text-decoration:none\" target=\"_blank\"><span style=\"font-size:16px\"><span style=\"font-family:helvetica neue,helvetica,arial,verdana,sans-serif\"><strong><span style=\"color:" + Colors.JOBS + "\">" + article.title + "</span></strong></span></span></a><br />\n" +
"<span style=\"font-size:15px\">" + article.description + "</span><br />"
val url = URL(article.link)
html += "<a href=\"" + url.host + "\" style=\"text-decoration:none\" target=\"_blank\"><strong><span style=\"font-size:15px; color:" + Colors.JOBS + "\">" + url.host + "</span></strong></a><br />\n" +
"<br />"
}
html = html.dropLast(6)
html += "</div></div>\n"
return html
}

fun createVideos(articles: List<Item>): String {
var html = "<div style=\"overflow: hidden;\">\n" +
"<div style=\"\n" +
"    width:30%;\n" +
"    float:left\">\n" +
"<p style=\"text-align: center;\"><br />\n" +
"<img data-file-id=\"2573213\" height=\"45\" src=\"https://gallery.mailchimp.com/f39692e245b94f7fb693b6d82/images/1190a3e2-9a27-4e7b-851e-20983326e25f.png\" style=\"border: 0px initial ; width: 45px; height: 45px; margin: 0px;\" width=\"45\" /><br />" +
"<strong><span style=\"font-size:11px\"><span style=\"font-family:helvetica neue,helvetica,arial,verdana,sans-serif\"><span style=\"color:#FFFFFF\"><span style=\"background-color:" + Colors.VIDEO + "; padding:5px 5px 5px 5px\">Videos</span></span></span></span></strong></p>\n" +
"</div>"
html += "<div style=\"  \n" +
"    width:70%;\n" +
"    float:left;\n" +
"    \"><br />\n"
for (article in articles) {
html += "<a href=\"" + article + "\" style=\"text-decoration:none\" target=\"_blank\"><span style=\"font-size:16px\"><span style=\"font-family:helvetica neue,helvetica,arial,verdana,sans-serif\"><strong><span style=\"color:" + Colors.VIDEO + "\">" + article.title + "</span></strong></span></span></a><br />\n" +
"<span style=\"font-size:15px\">" + article.description + "</span><br />"
val url = URL(article.link)
html += "<a href=\"" + url.host + "\" style=\"text-decoration:none\" target=\"_blank\"><strong><span style=\"font-size:15px; color:" + Colors.VIDEO + "\">" + url.host + "</span></strong></a><br />\n" +
"<br />"
}
html = html.dropLast(6)

html += "</div></div>\n"
return html
}

fun createPodcast(articles: List<Item>): String {
var html = "<div style=\"overflow: hidden;\">\n" +
"<div style=\"\n" +
"    width:30%;\n" +
"    float:left\">\n" +
"<p style=\"text-align: center;\"><br />\n" +
"<img data-file-id=\"2573205\" height=\"45\" src=\"https://gallery.mailchimp.com/f39692e245b94f7fb693b6d82/images/c5426cd8-115e-4ec2-939e-877c8f8d4f31.png\" style=\"border: 0px initial ; width: 45px; height: 45px; margin: 0px;\" width=\"45\" /><br />\n" +
"<strong><span style=\"font-size:11px\"><span style=\"font-family:helvetica neue,helvetica,arial,verdana,sans-serif\"><span style=\"color:#FFFFFF\"><span style=\"background-color:" + Colors.PODCAST + "; padding:5px 5px 5px 5px\">Podcast</span></span></span></span></strong></p>\n" +
"</div>"
html += "<div style=\"  \n" +
"    width:70%;\n" +
"    float:left;\n" +
"    \"><br />\n"
for (article in articles) {
html += "<a href=\"" + article + "\" style=\"text-decoration:none\" target=\"_blank\"><span style=\"font-size:16px\"><span style=\"font-family:helvetica neue,helvetica,arial,verdana,sans-serif\"><strong><span style=\"color:" + Colors.PODCAST + "\">" + article.title + "</span></strong></span></span></a><br />\n" +
"<span style=\"font-size:15px\">" + article.description + "</span><br />"
val url = URL(article.link)
html += "<a href=\"" + url.host + "\" style=\"text-decoration:none\" target=\"_blank\"><strong><span style=\"font-size:15px; color:" + Colors.PODCAST + "\">" + url.host + "</span></strong></a><br />\n" +
"<br />"
}
html = html.dropLast(6)

html += "</div></div>\n"
return html
}

fun createLibraries(libraries: List<Item>): String {
var html = "<div style=\"overflow: hidden;\">\n" +
"<div style=\"\n" +
"    width:30%;\n" +
"    float:left\">\n" +
"<p style=\"text-align: center;\"><br />\n" +
"<img data-file-id=\"2573205\" height=\"45\" src=\"https://gallery.mailchimp.com/f39692e245b94f7fb693b6d82/images/4837807f-7f56-4f5d-841a-784404090b26.png\" style=\"border: 0px initial ; width: 45px; height: 45px; margin: 0px;\" width=\"45\" /><br />\n" +
"<strong><span style=\"font-size:11px\"><span style=\"font-family:helvetica neue,helvetica,arial,verdana,sans-serif\"><span style=\"color:#FFFFFF\"><span style=\"background-color:" + Colors.LIBRARY + "; padding:5px 5px 5px 5px\">Libraries</span></span></span></span></strong></p>\n" +
"</div>"
html += "<div style=\"  \n" +
"    width:70%;\n" +
"    float:left;\n" +
"    \"><br />\n"
for (library in libraries) {
html += "<a href=\"" + library + "\" style=\"text-decoration:none\" target=\"_blank\"><span style=\"font-size:16px\"><span style=\"font-family:helvetica neue,helvetica,arial,verdana,sans-serif\"><strong><span style=\"color:" + Colors.LIBRARY + "\">" + library.title + "</span></strong></span></span></a><br />\n" +
"<span style=\"font-size:15px\">" + library.description + "</span><br />"
val url = URL(library.link)
html += "<a href=\"" + url.host + "\" style=\"text-decoration:none\" target=\"_blank\"><strong><span style=\"font-size:15px; color:" + Colors.LIBRARY + "\">" + url.host + "</span></strong></a><br />\n" +
"<br />"
}
html = html.dropLast(6)

html += "</div></div>\n"
return html
}

fun createConferences(articles: List<Item>): String {
var html = "<div style=\"overflow: hidden;\">\n" +
"<div style=\"\n" +
"    width:30%;\n" +
"    float:left\">\n" +
"<p style=\"text-align: center;\"><br />\n" +
"<img data-file-id=\"2573221\" height=\"45\" src=\"https://gallery.mailchimp.com/f39692e245b94f7fb693b6d82/images/d6e8808c-6fdc-4d79-bb44-b92ea4f9b47a.png\" style=\"border: 0px initial ; width: 45px; height: 45px; margin: 0px;\" width=\"45\" /><br />\n" +
"<strong><span style=\"font-size:11px\"><span style=\"font-family:helvetica neue,helvetica,arial,verdana,sans-serif\"><span style=\"color:#FFFFFF\"><span style=\"background-color:" + Colors.CONFERENCES + "; padding:5px 5px 5px 5px\">Conferences</span></span></span></span></strong></p>\n" +
"</div>"
html += "<div style=\"  \n" +
"    width:70%;\n" +
"    float:left;\n" +
"    \"><br />\n"
for (article in articles) {
html += "<a href=\"" + article + "\" style=\"text-decoration:none\" target=\"_blank\"><span style=\"font-size:16px\"><span style=\"font-family:helvetica neue,helvetica,arial,verdana,sans-serif\"><strong><span style=\"color:" + Colors.CONFERENCES + "\">" + article.title + "</span></strong></span></span></a><br />\n" +
"<span style=\"font-size:15px\">" + article.description + "</span><br />"
val url = URL(article.link)
html += "<a href=\"" + url.host + "\" style=\"text-decoration:none\" target=\"_blank\"><strong><span style=\"font-size:15px; color:" + Colors.CONFERENCES + "\">" + url.host + "</span></strong></a><br />\n" +
"<br />"
}
html = html.dropLast(6)
html += "</div></div>\n"
return html
}

fun createFooter(): String {
return "\n" +
"<p><u><strong>Contribute</strong></u></p>\n" +
"\n" +
"<p>We rely on sponsors to offer quality content every Sunday. If you would like to submit a sponsored link&nbsp;<a href=\"mailto:mailinglist@kotlinweekly.net?subject=Sponsoring%20for%20Kotlin%20Weekly\" target=\"_blank\">contact us</a>.</p>\n" +
"\n" +
"<p>If you want to submit an article for the next issue, please do also&nbsp;<a href=\"mailto:mailinglist@kotlinweekly.net?subject=Link%20for%20submission%20-%20Kotlin%20Weekly\" target=\"_blank\">drop us an email</a>.<br />\n" +
"<br />\n" +
"Thanks to&nbsp;<a data-saferedirecturl=\"https://www.google.com/url?q=https://www.jetbrains.com&amp;source=gmail&amp;ust=1574619446734000&amp;usg=AFQjCNF55EKdvRXZhaWpa1H0VQ-UJ5viiQ\" href=\"https://www.jetbrains.com/\" target=\"_blank\">JetBrains</a>&nbsp;for their support!</p>\n"
}**/