package com.databaseexam.neo4jmicroservice.repositories

import com.databaseexam.neo4jmicroservice.nodes.Institution
import org.springframework.data.neo4j.annotation.Query
import org.springframework.data.neo4j.repository.Neo4jRepository

interface InstitutionRepository : Neo4jRepository<Institution, Long> {

    @Query("MATCH (i:Institution) WHERE i.name = $0 return i")
    fun findByName(name: String): Institution
}