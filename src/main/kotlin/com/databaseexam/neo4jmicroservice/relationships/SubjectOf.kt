package com.databaseexam.neo4jmicroservice.relationships

import com.databaseexam.neo4jmicroservice.nodes.Course
import com.databaseexam.neo4jmicroservice.nodes.Subject
import org.neo4j.ogm.annotation.*

@RelationshipEntity(type = "SUBJECT_OF")
class SubjectOf(
        @Id
        @GeneratedValue
        val id: Long? = null,

        @StartNode
        val course: Course,

        @EndNode
        val subject: Subject
) {
}