package com.databaseexam.neo4jmicroservice.repositories

import com.databaseexam.neo4jmicroservice.nodes.Course
import org.springframework.data.neo4j.repository.Neo4jRepository

interface CourseRepository : Neo4jRepository<Course, Long> {
}