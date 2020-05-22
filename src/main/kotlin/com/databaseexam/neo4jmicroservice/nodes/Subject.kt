package com.databaseexam.neo4jmicroservice.nodes

import com.databaseexam.neo4jmicroservice.relationships.SubjectOf
import com.databaseexam.neo4jmicroservice.relationships.Teaches
import com.databaseexam.neo4jmicroservice.relationships.TeachesAt
import org.neo4j.ogm.annotation.Id
import org.neo4j.ogm.annotation.NodeEntity
import org.neo4j.ogm.annotation.Property
import org.neo4j.ogm.annotation.Relationship

@NodeEntity
class Subject(
        @Id
        @Property("name")
        val name: String? = null,

        @Relationship(type = "TEACHES", direction = "INCOMING")
        val teaches: MutableList<Teaches> = ArrayList(),

        @Relationship(type = "SUBJECT_OF", direction = "INCOMING")
        val instructors: MutableList<SubjectOf> = ArrayList()
) {
}