package com.ant.svserver

import org.quartz.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ShowTimeJobConfig(private val schedulerProperties: SchedulerProperties) {
    @Bean
    fun showTimeJobDetail(): JobDetail = JobBuilder
        .newJob(ShowTimeJob::class.java)
        .withIdentity("showTimeJob", schedulerProperties.permanentJobsGroupName)
        .storeDurably()
        .requestRecovery(true)
        .build()

    @Bean
    fun showTimeTrigger(): Trigger = TriggerBuilder.newTrigger()
        .forJob(showTimeJobDetail())
        .withIdentity("showTimeJobTrigger", schedulerProperties.permanentJobsGroupName)
        .withSchedule(CronScheduleBuilder.cronSchedule(schedulerProperties.showTimeJobCron))
        .build()
}