package com.ant.svserver.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import java.io.FileInputStream
import java.io.IOException

import java.util.Arrays;

const val BASE_URL = "https://vsmuta-f3e3d-default-rtdb.europe-west1.firebasedatabase.app"

class FireBaseInit {

    private val MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging"
    private val SCOPES = arrayOf(MESSAGING_SCOPE)
    val serviceAccount =
        FileInputStream("/home/ant/IdeaProjects/SVServer/src/main/resources/assets/vsmuta-f3e3d-firebase-adminsdk-3dzma-12edada204.json")

    fun firebaseInit() {

              val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setDatabaseUrl(BASE_URL)
            .build()
        FirebaseApp.initializeApp(options)
    }

    @Throws(IOException::class)
    private fun getAccessToken(): String? {
        val googleCredentials: GoogleCredentials = GoogleCredentials
            .fromStream(serviceAccount)
            .createScoped("SCOPES")
        googleCredentials.refreshAccessToken()
        return googleCredentials.accessToken.tokenValue
    }
}