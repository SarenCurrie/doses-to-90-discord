package com.sarencurrie.doses

import club.minnced.discord.webhook.WebhookClient
import org.jsoup.Jsoup
import kotlin.system.exitProcess

fun main() {
    checkCounts()
    exitProcess(0)
}

fun checkCounts() {

    val updates = mutableListOf<VaccineUpdate>()
    val dhbsToMonitor = listOf("Auckland", "Waitemata", "Counties Manukau")
    // https://www.health.govt.nz/our-work/diseases-and-conditions/covid-19-novel-coronavirus/covid-19-data-and-statistics/covid-19-vaccine-data

    Jsoup.connect("https://www.health.govt.nz/our-work/diseases-and-conditions/covid-19-novel-coronavirus/covid-19-data-and-statistics/covid-19-vaccine-data")
        .get().run {
        select(".table-style-two")[2].select("tr").forEach {
            val dhbName = it.select("th")[0].text()
            if (dhbsToMonitor.contains(dhbName)) {
                val dataPoints = it.select("td")
                val population = dataPoints[6].text().replace(",", "").toInt()
                val firstTotal = dataPoints[0].text().replace(",", "").toInt()
                val firstPercent = String.format("%.1f%%", firstTotal.toDouble() / population.toDouble() * 100)
                val firstToGo = dataPoints[2].text()
                val secondTotal = dataPoints[3].text().replace(",", "").toInt()
                val secondToGo = dataPoints[5].text()
                val secondPercent = String.format("%.1f%%", secondTotal.toDouble() / population.toDouble() * 100)
                println("$dhbName: 1%: $firstPercent; 1 to go: $firstToGo; 2%: $secondPercent; 2 to go: $secondToGo")
                updates.add(VaccineUpdate(dhbName, firstPercent, secondPercent, firstToGo, secondToGo))
            }
        }
    }

    val client = WebhookClient.withUrl(System.getenv("WEBHOOK_URL"))
    updates.chunked(10).map { client.send(it.map { update -> buildEmbed(update) }) }.forEach{ it.get() }
    client.close()
}
