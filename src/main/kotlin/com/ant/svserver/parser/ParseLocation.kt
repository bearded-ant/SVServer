package com.ant.svserver.parser

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class ParseLocation {
    fun getLocationStatus(): Elements {
        val url = "https://vsmuta.com/info/locs"
        val document: Document = Jsoup.connect(url).get()

        val all = document
            .select("div[class=m-auto]")


        return all
    }
}