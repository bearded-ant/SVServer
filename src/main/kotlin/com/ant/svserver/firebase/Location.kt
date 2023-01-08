package com.ant.svserver.firebase

data class Location(
    val name: String,
    val nextRun: String?,
    val status: String,
    val underAttackFlag: Boolean = false,
    val timeToAttack: Int?
)