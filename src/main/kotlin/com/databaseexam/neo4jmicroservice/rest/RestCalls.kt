package com.databaseexam.neo4jmicroservice.rest

import com.databaseexam.neo4jmicroservice.data.DataCourse
import com.databaseexam.neo4jmicroservice.nodes.Course
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class RestCalls(private val dataCourse: DataCourse) {

    @GetMapping("/courses")
    fun getAll(): List<Course> = dataCourse.getAll()

    @PostMapping("/course")
    fun createOne(@RequestBody course: Course) = dataCourse.createOne(course)
}