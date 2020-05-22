package com.databaseexam.neo4jmicroservice.nodes

import com.databaseexam.neo4jmicroservice.relationships.CourseAt
import com.databaseexam.neo4jmicroservice.relationships.InstructedBy
import com.databaseexam.neo4jmicroservice.relationships.SubjectOf
import com.databaseexam.neo4jmicroservice.relationships.TeachesAt
import org.neo4j.ogm.annotation.*

@NodeEntity
class Course(
        @Id
        @GeneratedValue
        val id: Long? = null,
        @Property("name")
        val name: String = "",
        @Property("participants")
        val participants: Int? = null,
        @Property("audited")
        val audited: Int? = null,
        @Property("date")
        val date: String? = null,

        @Relationship(type = "INSTRUCTED_BY")
        val instructors: MutableList<InstructedBy> = ArrayList(),

        @Relationship(type = "COURSE_AT")
        val courses: MutableList<CourseAt> = ArrayList(),

        @Relationship(type = "SUBJECT_OF")
        val subjects: MutableList<SubjectOf> = ArrayList()
) {

}