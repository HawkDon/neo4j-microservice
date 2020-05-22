package com.databaseexam.neo4jmicroservice.relationships

import com.databaseexam.neo4jmicroservice.nodes.Course
import com.databaseexam.neo4jmicroservice.nodes.Instructor
import org.neo4j.ogm.annotation.*

@RelationshipEntity(type = "INSTRUCTED_BY")
class InstructedBy(
        @Id
        @GeneratedValue
        val id: Long? = null,

        @StartNode
        val course: Course,

        @EndNode
        val instructor: Instructor
) {
}