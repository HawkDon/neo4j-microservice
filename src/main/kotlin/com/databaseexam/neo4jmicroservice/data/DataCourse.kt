package com.databaseexam.neo4jmicroservice.data

import com.databaseexam.neo4jmicroservice.driver
import com.databaseexam.neo4jmicroservice.enums.Operation
import com.databaseexam.neo4jmicroservice.getListOfCourses
import com.databaseexam.neo4jmicroservice.interfaces.DataLayer
import com.databaseexam.neo4jmicroservice.nodes.Course
import com.databaseexam.neo4jmicroservice.query
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component


@Component
class DataCourse  : DataLayer<Course> {
    override fun getAll(): List<Course> {
        val result = query("""MATCH (c:Course) return c""")
        val courses = getListOfCourses(result);
        return courses
    }


    override fun createOne(course: Course): ResponseEntity<HttpStatus> {
        TODO("dd")
    }

    override fun readOne(title: String): Course {
        TODO("Not yet implemented")
    }

    override fun updateOne(course: Course): ResponseEntity<HttpStatus> {
        TODO("Not yet implemented")
    }

    override fun deleteOne(title: String): ResponseEntity<HttpStatus> {
        TODO("Not yet implemented")
    }

    override fun filter(tags: List<String>, level: String, price: Int, operator: Operation): List<Course> {
        TODO("Not yet implemented")
    }
}