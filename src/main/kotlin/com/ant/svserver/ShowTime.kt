package com.ant.svserver

import com.ant.svserver.firebase.Location
import com.ant.svserver.parser.ParseLocation
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.stereotype.Service
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

        val locationObjectList = ParseLocation().createStatusObjects()

        val dataHashMap = mutableMapOf<String, Location>()

        for (i in 0..locationObjectList.lastIndex) {
            dataHashMap.put(locationObjectList[i].name, locationObjectList[i])
            if (locationObjectList[i].underAttackFlag)
                sendMessage(locationObjectList[i])
        }
        ref.updateChildrenAsync(dataHashMap as Map<String, Any>?)

        return nowLocalTime;
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