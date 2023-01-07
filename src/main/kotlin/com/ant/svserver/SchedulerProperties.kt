package com.ant.svserver


import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties(prefix = "scheduler")
@ConstructorBinding
data class SchedulerProperties(
    val permanentJobsGroupName: String = "PERMANENT",
    val showTimeJobCron: String = "0 0/1 * * * ?"
)
