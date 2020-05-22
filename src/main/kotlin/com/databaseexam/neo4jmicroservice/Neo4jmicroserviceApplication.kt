package com.databaseexam.neo4jmicroservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Neo4jmicroserviceApplication

fun main(args: Array<String>) {
	runApplication<Neo4jmicroserviceApplication>(*args)
}
