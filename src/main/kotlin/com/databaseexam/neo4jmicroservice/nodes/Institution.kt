package com.databaseexam.neo4jmicroservice.nodes

import org.neo4j.ogm.annotation.*

@NodeEntity
class Institution(
        val id: String,
        val name: String
)