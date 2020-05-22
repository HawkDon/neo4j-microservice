package com.databaseexam.neo4jmicroservice.relationships

import com.databaseexam.neo4jmicroservice.nodes.Instructor
import com.databaseexam.neo4jmicroservice.nodes.Subject
import org.neo4j.ogm.annotation.*

@RelationshipEntity(type = "Teaches")
class Teaches(
        @Id
        @GeneratedValue
        val id: Long? = null,

        @StartNode
        val instructor: Instructor,

        @EndNode
        val subject: Subject
) {

}