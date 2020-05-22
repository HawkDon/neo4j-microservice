package com.databaseexam.neo4jmicroservice.data

import com.databaseexam.neo4jmicroservice.nodes.Course
import com.databaseexam.neo4jmicroservice.nodes.Institution
import com.databaseexam.neo4jmicroservice.nodes.Instructor
import com.databaseexam.neo4jmicroservice.nodes.Subject
import com.databaseexam.neo4jmicroservice.relationships.*
import com.databaseexam.neo4jmicroservice.repositories.CourseRepository
import com.databaseexam.neo4jmicroservice.repositories.InstitutionRepository
import com.databaseexam.neo4jmicroservice.repositories.InstructorRepository
import com.databaseexam.neo4jmicroservice.repositories.SubjectRepository
import org.springframework.stereotype.Component

@Component
class PopulateData(
        private val subjectRepository: SubjectRepository,
        private val instructorRepository: InstructorRepository,
        private val institutionRepository: InstitutionRepository,
        private val courseRepository: CourseRepository
) {

    fun populateNodesFromCSV(records: MutableList<Array<String>>) {
        // Temp for now. Used for cleanup
        subjectRepository.deleteAll()
        instructorRepository.deleteAll()
        institutionRepository.deleteAll()
        courseRepository.deleteAll()

        val allSubjects = records.map {
            val subjects: MutableList<String> = it[5].split(", ") as MutableList<String>
            if(subjects[subjects.size - 1].substring(0..3) == "and ") {
                subjects[subjects.size - 1] = subjects[subjects.size - 1].substring(4);
            }
            subjects
        }.flatten().toSet()

        for (key in allSubjects) {
            subjectRepository.save(Subject(name = key))
        }

        val instructors = records.map { it[4].split(", ") }.flatten().toSet()

        for (instructor in instructors) {
            instructorRepository.save(Instructor(name = instructor))
        }

        val institutions = records.map { it[0] }.toSet()

        for (institution in institutions) {
            institutionRepository.save(Institution(name = institution))
        }

        val courses = records.map { Course(name = it[3], participants = it[8].toInt(), audited = it[9].toInt(), date = it[2]) }

        for (course in courses) {
            courseRepository.save(course)
        }
    }

    fun createInstructorRelationShips(records: MutableList<Array<String>>) {

        val instructors = instructorRepository.findAll().toList()
        for(instructor in instructors) {
            val recordsOfParticipation = records.filter {
                var isFound = false
                val currentInstructors = it[4].split(", ")
                if(currentInstructors.size == 1) {
                    if(currentInstructors[0] == instructor.name) {
                        isFound = true
                    }
                } else {
                    var index = 0
                    outer@ while (index < currentInstructors.size - 1) {
                        for (element in currentInstructors) {
                            if(element == instructor.name) {
                                isFound = true
                                break@outer
                            }
                            index += 1
                        }
                    }
                }
                isFound
            }


            val teachesAtList = recordsOfParticipation.map { it[0] }.toSet()

            for(institution in teachesAtList) {
                val institution = institutionRepository.findByName(institution)
                val teachesAtRelationShip = TeachesAt(institution = institution, instructor = instructor)
                instructor.teachesAt.add(teachesAtRelationShip)
                institution.instructors.add(teachesAtRelationShip)
                institutionRepository.save(institution)
                instructorRepository.save(instructor)
            }
            val subjectsInstructorTeaches = recordsOfParticipation.map {
                val subjects: MutableList<String> = it[5].split(", ") as MutableList<String>
                if(subjects[subjects.size - 1].substring(0..3) == "and ") {
                    subjects[subjects.size - 1] = subjects[subjects.size - 1].substring(4);
                }
                subjects
            }.flatten().toSet()

            for (subject in subjectsInstructorTeaches) {
                val subject = subjectRepository.findById(subject).get()
                val teachesRelationShip = Teaches(instructor = instructor, subject = subject)
                subject.teaches.add(teachesRelationShip)
                instructor.teaches.add(teachesRelationShip)
                subjectRepository.save(subject)
                instructorRepository.save(instructor)
            }
        }
    }

    fun createCourseRelationShips(records: MutableList<Array<String>>) {

        val courses = courseRepository.findAll()

        for (course in courses) {
            val record = records.filter { it[3] == course.name }[0]

            val instructors = record[4].split(", ")

            for (instructor in instructors) {
                val instructorFromNeo4j = instructorRepository.findByName(instructor)
                val instructedByRelationShip = InstructedBy(instructor = instructorFromNeo4j, course = course)
                course.instructors.add(instructedByRelationShip)
                instructorFromNeo4j.instructors.add(instructedByRelationShip)
                courseRepository.save(course)
                instructorRepository.save(instructorFromNeo4j)
            }

            val subjects = record[5].split(", ").map {
                if(it.substring(0..3) == "and ") {
                    it.substring(4)
                } else {
                    it
                }
            }

            for (subject in subjects) {
                val subjectFromNeo4j = subjectRepository.findByName(subject)
                val subjectOfRelationship = SubjectOf(subject = subjectFromNeo4j, course = course)
                course.subjects.add(subjectOfRelationship)
                subjectFromNeo4j.instructors.add(subjectOfRelationship)
                courseRepository.save(course)
                subjectRepository.save(subjectFromNeo4j)
            }


            val institution = record[0]

            val institutionFromNeo4j = institutionRepository.findByName(institution)
            val relationship = CourseAt(course = course, institution = institutionFromNeo4j)
            course.courses.add(relationship)
            institutionFromNeo4j.courses.add(relationship)
            courseRepository.save(course)
            institutionRepository.save(institutionFromNeo4j)

        }
    }
}