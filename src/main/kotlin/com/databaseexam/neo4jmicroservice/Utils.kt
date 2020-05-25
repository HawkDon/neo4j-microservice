package com.databaseexam.neo4jmicroservice

import com.databaseexam.neo4jmicroservice.nodes.Course
import org.neo4j.driver.Result

fun getAllSubjects(records: MutableList<Array<String>>) = records.map {
    val subjects: MutableList<String> = it[5].split(", ") as MutableList<String>
    if(subjects[subjects.size - 1].substring(0..3) == "and ") {
        subjects[subjects.size - 1] = subjects[subjects.size - 1].substring(4);
    }
    subjects
}.flatten().toSet()

fun query(query: String) = driver.session().run(query)


fun getListOfCourses(result: Result): MutableList<Course> {
    val courses = mutableListOf<Course>()
    while(result.hasNext()) {
        val course = result.next()[0].asMap().toCourse()
        courses.add(course)
    }
    return courses
}

fun MutableMap<String, Any>.toCourse(): Course {
    val course = Course(
            id = this["id"] as String,
            name = this["name"] as String,
            participants = this["participants"].toString().toInt(),
            audited = this["audited"].toString().toInt(),
            date = this["date"] as String,
            price = this["price"].toString().toInt()
    )
    return course
}