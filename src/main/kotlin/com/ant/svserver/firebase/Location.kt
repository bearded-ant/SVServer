package com.ant.svserver.firebase

import java.time.LocalDateTime

data class Location(
    val name: String,
    val nextRun: String?,
    val status: String,
    val underAttackFlag: Boolean = false,
    val attackStartTime: Int?
)