package com.sarencurrie.doses

import club.minnced.discord.webhook.send.WebhookEmbed
import club.minnced.discord.webhook.send.WebhookEmbedBuilder

fun buildEmbed(
    data: VaccineUpdate
): WebhookEmbed {
    val builder = WebhookEmbedBuilder()
    val color = if (data.secondDosesLeft == "0") {
        0x11cf73
    } else {
        val dosesLeft = data.secondDosesLeft.replace(",", "").toInt()
        scaleRed(dosesLeft) * 256 * 256 + scaleGreen(dosesLeft) * 256 + 73
    }
    try {
        builder
            .setColor(color)
            .setTitle(
                WebhookEmbed.EmbedTitle("Vaccine Update for ${data.dhb}", null)
            )
            .setAuthor(
                WebhookEmbed.EmbedAuthor(
                    "Ministry of Health",
                    null,
                    "https://www.health.govt.nz/our-work/diseases-and-conditions/covid-19-novel-coronavirus/covid-19-data-and-statistics/covid-19-vaccine-data"
                )
            )
            .addField(WebhookEmbed.EmbedField(true, "First doses", data.firstDosesPercent))
            .addField(WebhookEmbed.EmbedField(true, "Second doses", data.secondDosesPercent))
        if (data.secondDosesLeft != "0") {
            builder.addField(WebhookEmbed.EmbedField(true, "Second doses to 90%", data.secondDosesLeft))
        }
    } catch (e: Exception) {
        println(data)
        throw e
    }
    return builder.build()
}
const val max = 25000

fun scaleRed(dosesLeft: Int): Int {
    val from = 0xff
    val to = 0x33

    if (dosesLeft > max) {
        return from
    }

    val scale = (dosesLeft / max.toFloat() * (from - to)).toInt()

    return scale + to
}

fun scaleGreen(dosesLeft: Int): Int {
    val from = 0x11
    val to = 0xaa

    if (dosesLeft > max) {
        return from
    }

    val scale = (dosesLeft / max.toFloat() * (to - from)).toInt()

    return to - scale
}