package com.sarencurrie.doses

import club.minnced.discord.webhook.send.WebhookEmbed
import club.minnced.discord.webhook.send.WebhookEmbedBuilder

fun buildEmbed(
    data: VaccineUpdate
): WebhookEmbed {
    val builder = WebhookEmbedBuilder()
    try {
        builder
            .setColor(0x11cf73)
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