package com.databaseexam.neo4jmicroservice.data

import com.databaseexam.neo4jmicroservice.enums.Operation
import com.databaseexam.neo4jmicroservice.getListOfCourses
import com.databaseexam.neo4jmicroservice.interfaces.DataLayer
import com.databaseexam.neo4jmicroservice.nodes.Course
import com.databaseexam.neo4jmicroservice.query
import com.databaseexam.neo4jmicroservice.toCourse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.lang.Exception
import java.util.*


@Component
class DataCourse  : DataLayer<Course> {
    override fun getAll(): List<Course> {
        val result = query("""MATCH (c:Course) return c""")
        val courses = getListOfCourses(result);
        return courses
    }


    override fun createOne(course: Course): ResponseEntity<HttpStatus> {
        query("""CREATE (c:Course {id: "${UUID.randomUUID()}",
                |name: "${course.name}", 
                |participants: "${course.participants}",
                |audited: "${course.audited}",
                |date: "${course.date}",
                |price: "${course.price}"})""".trimMargin())
        return ResponseEntity(HttpStatus.CREATED)
    }

    override fun readOne(title: String): Course {
        val result = query("""MATCH (c:Course) WHERE c.name = "$title" return c""")
        val course = result.next()[0].asMap().toCourse()
        return course
    }

    override fun updateOne(course: Course): ResponseEntity<HttpStatus> {
        try {
            query("""
                |MATCH (c:Course {id: "${course.id}" })
                |SET c.name = "${course.name}"
                |SET c.participants = "${course.participants}"
                |SET c.audited = "${course.audited}"
                |SET c.date = "${course.date}"
                |SET c.price = "${course.price}"
            """.trimMargin())
        } catch (e: Exception) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity(HttpStatus.OK)
    }

    override fun deleteOne(id: String): ResponseEntity<HttpStatus> {
        try {
            query("""MATCH (c:Course) WHERE c.id = "$id" DELETE c""")
        } catch (e: Exception) {
            return ResponseEntity(HttpStatus.BAD_REQUEST)
        }
        return ResponseEntity(HttpStatus.OK)
    }

    fun filter(tags: List<String>?, level: String?, price: Int?, operator: Operation?): List<Course> {
        var query = "MATCH (course:Course)-[:SUBJECT_OF]->(subject:Subject) " +
                "WITH course, subject " +
                "MATCH (course)-[:HAS_LEVEL]->(difficulty:Difficulty) " +
                "WHERE "

        tags?.run {
            val tagsQuery = this.map { "\"$it\"" }
            query += "subject.name IN $tagsQuery "
        }

        level?.run {
            query += if(query.substring(query.length - 6) == "WHERE ") {
                "difficulty.name = \"$this\" "
            } else {
                "AND difficulty.name = \"$this\" "
            }
        }
        price?.run {
            if(operator != null) {
                when(operator) {
                    Operation.greaterThan -> {
                        query += if(query.substring(query.length - 6) == "WHERE ") {
                            "course.price > $this "
                        } else {
                            "AND course.price > $this "
                        }
                    }
                    Operation.lessThan -> {
                        query += if(query.substring(query.length - 6) == "WHERE ") {
                            "course.price < $this "
                        } else {
                            "AND course.price < $this "
                        }
                    }
                }
            } else {
                query += if(query.substring(query.length - 6) == "WHERE ") {
                    "course.price = $this "
                } else {
                    "AND course.price = $this "
                }
            }

        }
        val result = query(query + "RETURN course")

        return getListOfCourses(result)
    }
}

/*
MATCH (course:Course)-[:SUBJECT_OF]->(subject:Subject)
WITH course, subject
MATCH (course)-[:HAS_LEVEL]->(difficulty:Difficulty)
WHERE subject.name IN ["Government"]
AND
difficulty.name = "Expert"
AND
course.price < 40
return course
*/