package com.databaseexam.neo4jmicroservice.nodes
data class Course(
        val id: String,
        val name: String,
        val participants: Int,
        val audited: Int,
        val date: String,
        val price: Int
)