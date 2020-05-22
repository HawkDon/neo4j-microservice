package com.databaseexam.neo4jmicroservice.repositories

import com.databaseexam.neo4jmicroservice.nodes.Institution
import com.databaseexam.neo4jmicroservice.nodes.Instructor
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.Neo4jRepository

interface InstructorRepository : Neo4jRepository<Instructor, Long> {

    @Query("MATCH (i:Instructor) WHERE i.name = $0 return i")
    fun findByName(name: String): Instructor
}