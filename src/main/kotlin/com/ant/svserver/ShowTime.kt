package com.ant.svserver

import com.ant.svserver.firebase.Location
import com.ant.svserver.parser.ParseLocation
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.jsoup.select.Elements
import org.springframework.stereotype.Service
import java.lang.StringBuilder
import java.time.LocalDateTime


@Service
class ShowTime {
    private val ref = FirebaseDatabase.getInstance().getReference("locstatus")

    fun showTime(lastExecutionDateTime: LocalDateTime?): LocalDateTime {
        println()
        lastExecutionDateTime?.let {
            println("Previous execution time: $it")
        } ?: println("No previous executions found")
        val nowLocalTime = LocalDateTime.now()
        println("Current execution time: $nowLocalTime")

        val parserLocation = ParseLocation().getLocationStatus()

        val locationObjectList = createStatusObjects(parserLocation)

        val dataHashMap = mutableMapOf<String, Location>()

        for (i in 0..locationObjectList.lastIndex) {
            dataHashMap.put(locationObjectList[i].name, locationObjectList[i])
            if (locationObjectList[i].underAttackFlag)
                sendMessage(locationObjectList[i])
        }
        ref.updateChildrenAsync(dataHashMap as Map<String, Any>?)

        return nowLocalTime;
    }

    private fun createStatusObjects(parserLocation: Elements): List<Location> {
        val locationObjectList = mutableListOf<Location>()
        var attackStartTime = 0

        for (i in 0 until parserLocation.lastIndex) {

            val nameLocation = parserLocation[i]
                .select("div[class=mt-2]")
                .select("b")
                .text()

            val nexAttackTime = parserLocation[i]
                .selectFirst("div[class=mt-3]")?.text()

            val statusLocation = StringBuilder("")
            var fightFlag = false

            if (parserLocation[i]
                    .select("div[class=mt-3]")
                    .select("div[class=text-danger]")
                    .text()
                    .isNotEmpty()
            ) {
                statusLocation.append(
                    parserLocation[i]
                        .select("div[class=mt-3]")
                        .select("div[class=text-danger]")
                        .text()
                )
                fightFlag = true
                attackStartTime = 0
            } else if (parserLocation[i].select("div[class=text-warning fw-bold]").isNotEmpty())
                statusLocation.append(parserLocation[i].select("div[class=text-warning fw-bold]").text())
            else if (parserLocation[i].select("div[class=text-success fw-bold]").isNotEmpty())
                statusLocation.append(parserLocation[i].select("div[class=text-success fw-bold]").text())
            else {
                statusLocation.append(
                    "До крика осталось: ${
                        parserLocation[i]
                            .select("div[class=mt-3]")
                            .select("b")
                            .text()
                    }"
                )
                fightFlag = false
            }

            locationObjectList.add(
                Location(
                    name = nameLocation,
                    nextRun = nexAttackTime,
                    status = statusLocation.toString(),
                    underAttackFlag = fightFlag,
                    attackStartTime = attackStartTime
                )
            )
        }

        println(locationObjectList)
        return locationObjectList
    }

    private fun sendMessage(location: Location) {
        val topic = "/topics/location"

        val message: Message = Message.builder()
            .setTopic(topic)
            .setNotification(
                Notification.builder().setTitle(location.name).setBody("under attack ${location.status}").build()
            )
            .build()

        val response = FirebaseMessaging.getInstance().send(message)
        println("Successfully sent message: $response")
    }
}