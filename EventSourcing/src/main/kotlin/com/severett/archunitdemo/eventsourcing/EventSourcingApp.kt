package com.severett.archunitdemo.eventsourcing

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class EventSourcingApp

fun main(args: Array<String>) {
    SpringApplication.run(EventSourcingApp::class.java, *args)
}
