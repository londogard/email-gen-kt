package com.londogard.emailgen

object EmailHelper {
    val styles = mapOf("--border-radius" to "5px",
    "--box-shadow" to "2px 2px 10px",
    "--color" to "#118bee",
    "--color-accent" to "#118bee0b",
    "--color-bg" to "#fff",
    "--color-bg-secondary" to "#e9e9e9",
    "--color-secondary" to "#920de9",
    "--color-secondary-accent" to "#920de90b",
    "--color-shadow" to "#f4f4f4",
    "--color-text" to "#000",
    "--color-text-secondary" to "#999",
    "--hover-brightness" to "1.2",
    "--justify-important" to "center",
    "--justify-normal" to "left",
    "--line-height" to "150%",
    "--width-card" to "285px",
    "--width-card-medium" to "460px",
    "--width-card-wide" to "800px",
    "--width-content" to "1080px")

    fun getEmailStyle(): String = """
    <style>
    ${javaClass
        .getResourceAsStream("/email.css")
        .bufferedReader().readText()
        .let { lines ->
            styles.entries.fold(lines) { acc, (key, value) ->
                acc.replace("var($key)", value)
            }
        }}
    </style>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    """

    fun getFullFileText(path: String): String =
        javaClass.getResourceAsStream(path).bufferedReader().readText()

    const val buSlackArchive = "https://buitsouth.slack.com/archives"
}