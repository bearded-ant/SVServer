package com.ant.SVServer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SvServerApplication

fun main(args: Array<String>) {
	runApplication<SvServerApplication>(*args)
}
