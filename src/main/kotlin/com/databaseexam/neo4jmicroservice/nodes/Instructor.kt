package com.databaseexam.neo4jmicroservice.nodes

import com.databaseexam.neo4jmicroservice.relationships.InstructedBy
import com.databaseexam.neo4jmicroservice.relationships.Teaches
import com.databaseexam.neo4jmicroservice.relationships.TeachesAt
import org.neo4j.ogm.annotation.*

@NodeEntity
class Instructor(
        @Id
        @GeneratedValue
        val id: Long? = null,
        @Property("name")
        val name: String? = "",

        @Relationship(type = "Teaches")
        val teaches: MutableList<Teaches> = ArrayList(),

        @Relationship(type = "TeachesAt")
        val teachesAt: MutableList<TeachesAt> = ArrayList(),

        @Relationship(type = "INSTRUCTED_BY", direction = "INCOMING")
        val instructors: MutableList<InstructedBy> = ArrayList()
) {
}