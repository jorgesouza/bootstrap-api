package com.example.bootstrap.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.web.config.EnableSpringDataWebSupport

@SpringBootApplication
@EnableSpringDataWebSupport // Para dar suporte a paginação
class BootstrapApiApplication

fun main(args: Array<String>) {
	runApplication<BootstrapApiApplication>(*args)
}
