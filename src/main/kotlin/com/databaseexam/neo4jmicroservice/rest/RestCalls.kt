package com.databaseexam.neo4jmicroservice.rest

import com.databaseexam.neo4jmicroservice.data.DataCourse
import com.databaseexam.neo4jmicroservice.enums.Operation
import com.databaseexam.neo4jmicroservice.nodes.Course
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class RestCalls(private val dataCourse: DataCourse) {

    @GetMapping("/courses")
    fun getAll(): List<Course> = dataCourse.getAll()

    @PostMapping("/course")
    fun createOne(@RequestBody course: Course) = dataCourse.createOne(course)

    @GetMapping("/course")
    fun readOne(@RequestParam("title") title: String) = dataCourse.readOne(title)

    @PutMapping("/course")
    fun updateOne(@RequestBody course: Course) = dataCourse.updateOne(course)

    @DeleteMapping("/course")
    fun deleteOne(@RequestParam("id") id: String) = dataCourse.deleteOne(id)

    @GetMapping("/courses/filter")
    fun filterCourses(@RequestParam("tags") tags: List<String>?, @RequestParam("difficulty") level: String?, @RequestParam("price") price: Int?, @RequestParam("operation") operator: Operation?): List<Course> = dataCourse.filter(tags, level, price, operator)
}