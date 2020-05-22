package com.databaseexam.neo4jmicroservice.relationships

import com.databaseexam.neo4jmicroservice.nodes.Institution
import com.databaseexam.neo4jmicroservice.nodes.Instructor
import org.neo4j.ogm.annotation.*

@RelationshipEntity(type = "TEACHES_AT")
class TeachesAt(
        @Id
        @GeneratedValue
        val id: Long? = null,

        @StartNode
        val instructor: Instructor,

        @EndNode
        val institution: Institution
) {

}