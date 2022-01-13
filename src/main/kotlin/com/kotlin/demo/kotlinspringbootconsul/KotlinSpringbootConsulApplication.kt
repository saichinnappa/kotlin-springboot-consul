package com.kotlin.demo.kotlinspringbootconsul

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class KotlinSpringbootConsulApplication

fun main(args: Array<String>) {
    SpringApplication.run(KotlinSpringbootConsulApplication::class.java, *args)
}
