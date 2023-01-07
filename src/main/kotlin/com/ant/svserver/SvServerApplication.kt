package com.ant.svserver

import com.ant.svserver.firebase.FireBaseInit
import com.ant.svserver.parser.ParseLocation
import com.google.api.core.ApiFuture
import com.google.firebase.database.FirebaseDatabase
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import java.util.concurrent.TimeUnit

@SpringBootApplication
@ConfigurationPropertiesScan
class SvServerApplication

fun main(args: Array<String>) {
	FireBaseInit().firebaseInit()

	runApplication<SvServerApplication>(*args)
}
