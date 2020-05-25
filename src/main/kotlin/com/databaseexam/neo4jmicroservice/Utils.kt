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
        val record = result.next()[0].asMap()
        val course = Course(
                id = record["id"] as String,
                name = record["name"] as String,
                participants = record["participants"].toString().toInt(),
                audited = record["audited"].toString().toInt(),
                date = record["date"] as String,
                price = record["price"].toString().toInt())
        courses.add(course)
    }
    return courses
}