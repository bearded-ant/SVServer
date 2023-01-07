package com.ant.svserver.parser

import com.ant.svserver.firebase.Location
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class ParseLocation {

    fun createStatusObjects(): List<Location> {
        val locationObjectList = mutableListOf<Location>()
        val parserLocation = parseLocationFromUrl()

        var attackStartTime = 0

        for (i in 0 until parserLocation.lastIndex) {

            val nameLocation = getLocationName(parserLocation, i)
            val nexAttackTime = getNexAttackTime(parserLocation, i)
            val statusLocation = getStatusLocation(parserLocation, i)

            locationObjectList.add(
                Location(
                    name = nameLocation,
                    nextRun = nexAttackTime,
                    status = statusLocation.status,
                    underAttackFlag = statusLocation.attackFlag,
                    attackStartTime = attackStartTime
                )
            )
        }

        println(locationObjectList)
        return locationObjectList
    }

    private fun parseLocationFromUrl(): Elements {
        val url = "https://vsmuta.com/info/locs"
        val document: Document = Jsoup.connect(url).get()

        return document.select("div[class=m-auto]")
    }

    private fun getStatusLocation(parserLocation: Elements, i: Int): FlagAndString {
        val statusDanger = parserLocation[i]
            .select("div[class=mt-3]")
            .select("div[class=text-danger]")
            .text()

        val statusXXX = parserLocation[i]
            .select("div[class=text-warning fw-bold]")
            .text()

        val statusYYY = parserLocation[i]
            .select("div[class=text-success fw-bold]")
            .text()

        val statusNormal = "До крика осталось: ${
            parserLocation[i]
                .select("div[class=mt-3]")
                .select("b")
                .text()
        }"

        return when (true) {
            statusDanger.isNotBlank() -> FlagAndString(true, statusDanger)
            statusXXX.isNotBlank() -> FlagAndString(false, statusXXX)
            statusYYY.isNotBlank() -> FlagAndString(false, statusYYY)
            else -> FlagAndString(false, statusNormal)
        }
    }

    private fun getNexAttackTime(parserLocation: Elements, i: Int) = parserLocation[i]
        .selectFirst("div[class=mt-3]")?.text()

    private fun getLocationName(parserLocation: Elements, i: Int) = parserLocation[i]
        .select("div[class=mt-2]")
        .select("b")
        .text()
}