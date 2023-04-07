package com.severett.archunitdemo.activerecord

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class ActiveRecordApp

fun main(args: Array<String>) {
    SpringApplication.run(ActiveRecordApp::class.java, *args)
}