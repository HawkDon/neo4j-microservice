package com.databaseexam.neo4jmicroservice.repositories

import com.databaseexam.neo4jmicroservice.nodes.Instructor
import com.databaseexam.neo4jmicroservice.nodes.Subject
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.Neo4jRepository

interface SubjectRepository : Neo4jRepository<Subject, String> {

    @Query("MATCH (s:Subject) WHERE s.name = $0 return s")
    fun findByName(name: String): Subject
}