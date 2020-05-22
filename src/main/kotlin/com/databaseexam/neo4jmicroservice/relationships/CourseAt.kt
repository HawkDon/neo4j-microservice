package com.databaseexam.neo4jmicroservice.relationships

import com.databaseexam.neo4jmicroservice.nodes.Course
import com.databaseexam.neo4jmicroservice.nodes.Institution
import org.neo4j.ogm.annotation.*

@RelationshipEntity(type = "COURSE_AT")
class CourseAt(
        @Id
        @GeneratedValue
        val id: Long? = null,

        @StartNode
        val course: Course,

        @EndNode
        val institution: Institution
) {
}