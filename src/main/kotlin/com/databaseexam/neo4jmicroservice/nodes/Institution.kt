package com.databaseexam.neo4jmicroservice.nodes

import com.databaseexam.neo4jmicroservice.relationships.CourseAt
import com.databaseexam.neo4jmicroservice.relationships.TeachesAt
import org.neo4j.ogm.annotation.*

@NodeEntity
class Institution(
        @Id
        @GeneratedValue
        val id: Long? = null,
        @Property("name")
        val name: String = "",

        @Relationship(type = "TEACHES_AT", direction = "INCOMING")
        val instructors: MutableList<TeachesAt> = ArrayList(),

        @Relationship(type = "COURSE_AT", direction = "INCOMING")
        val courses: MutableList<CourseAt> = ArrayList()
) {
}