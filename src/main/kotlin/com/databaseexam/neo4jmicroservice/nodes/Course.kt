package com.databaseexam.neo4jmicroservice.nodes

import org.neo4j.ogm.annotation.*

@NodeEntity
class Course(
        val id: String,
        val name: String,
        val participants: Int,
        val audited: Int,
        val date: String,
        val price: Int
)