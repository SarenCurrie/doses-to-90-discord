package com.sarencurrie.doses

data class VaccineUpdate(
    val dhb: String,
    val firstDosesPercent: String,
    val secondDosesPercent: String,
    val firstDosesLeft: String,
    val secondDosesLeft: String,
)