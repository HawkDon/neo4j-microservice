package com.databaseexam.neo4jmicroservice

import com.databaseexam.neo4jmicroservice.nodes.*
import com.opencsv.CSVReader
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.Driver
import org.neo4j.driver.GraphDatabase
import org.springframework.stereotype.Component
import java.io.FileReader
import java.util.*
import javax.annotation.PostConstruct
import kotlin.random.Random

val driver: Driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "password"))

@Component
class Database{

    val difficulties = arrayListOf<String>(
            "Beginner",
            "Intermediate",
            "Expert"
    )
    @PostConstruct
    fun initIt() {

        // Wipe all data to begin with
        query("""match (a) -[r] -> () delete a, r""")
        query("""match (a) delete a""")
        // Get records from CSV file
        val records = getRecordsFromCSVFile()
        // Create nodes
        populateNodesFromCSV(records)
        // Create instructor relationships
        createInstructorRelationShips(records)
        // Create course relationships
        createCourseRelationShips(records)

    }

    fun getRecordsFromCSVFile(): MutableList<Array<String>> {
        val records = mutableListOf<Array<String>>()
        val csvReader = CSVReader(FileReader(javaClass.classLoader.getResource("init.csv").path))

        var values: Array<String>?

        // We skip initial because we do not need the titles
        csvReader.readNext()

        values = csvReader.readNext();
        while (values != null) {
            records.add(values)
            val current = csvReader.readNext()
            values = current
        }
        return records
    }

    fun populateNodesFromCSV(records: MutableList<Array<String>>) {
        // Temp for now. Used for cleanup

        val allSubjects = getAllSubjects(records)

        for (key in allSubjects) {
            query("""CREATE (s:Subject {name: "$key"})""")
        }

        val instructors = records.map { it[4].split(", ") }.flatten().toSet()

        for (instructor in instructors) {
            val id = UUID.randomUUID().toString()
            query("""CREATE (i:Instructor {id: "$id", name: "$instructor"})""")
        }

        val institutions = records.map { it[0] }.toSet()

        for (institution in institutions) {
            val id = UUID.randomUUID().toString()
            query("""CREATE (i:Institution {id: "$id", name: "$institution"})""")
        }

        val courses = records.map { Course(id = "0", name = it[3], participants = it[8].toInt(), audited = it[9].toInt(), date = it[2], price = Random.nextInt(0,100)) }

        for (course in courses) {
            val id = UUID.randomUUID().toString()
            query("""
            CREATE (c:Course {
            id: "$id",
            name: "${course.name}",
            participants: "${course.participants}",
            audited: "${course.audited}",
            date: "${course.date}",
            price: ${course.price}})
            """.trimIndent())
        }


        for (diff in difficulties) {
            query("""
            CREATE (d:Difficulty {
            name: "$diff"
            })
            """.trimIndent())
        }

    }

    fun createInstructorRelationShips(records: MutableList<Array<String>>) {

        val result = query("MATCH (i:Instructor) return i")
        val instructors = mutableListOf<Instructor>()
        while(result.hasNext()) {
            val record = result.next()[0].asMap()
            val instructor = Instructor(id = record["id"] as String, name = record["name"] as String)
            instructors.add(instructor)
        }

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
                val result = query("""MATCH (i:Institution) WHERE i.name = "$institution" return i""")
                val record = result.next()[0].asMap()
                val databaseInstitution = Institution(id = record["id"] as String, name = record["name"] as String)
                query("""
                    MATCH (instructor:Instructor {id: "${instructor.id}"}), (institution:Institution {id: "${databaseInstitution.id}"})
                    CREATE (instructor)-[:TEACHES_AT]->(institution)
                """.trimIndent())
            }

            val subjectsInstructorTeaches = recordsOfParticipation.map {
                val subjects: MutableList<String> = it[5].split(", ") as MutableList<String>
                if(subjects[subjects.size - 1].substring(0..3) == "and ") {
                    subjects[subjects.size - 1] = subjects[subjects.size - 1].substring(4);
                }
                subjects
            }.flatten().toSet()


            for (subject in subjectsInstructorTeaches) {
                val result = query("""MATCH (s:Subject) WHERE s.name = "$subject" return s""")
                val record = result.next()[0].asMap()
                val databaseSubject = Subject(name = record["name"] as String)
                query("""
                    MATCH (instructor:Instructor {id: "${instructor.id}"}), (subject:Subject {name: "${databaseSubject.name}"})
                    CREATE (instructor)-[:TEACHES]->(subject)
                """.trimIndent())
            }
        }
    }

    fun createCourseRelationShips(records: MutableList<Array<String>>) {

        val result = query("""MATCH (c:Course) return c""")
        val courses = getListOfCourses(result)

        for (course in courses) {
            val record = records.filter { it[3] == course.name }[0]

            val instructors = record[4].split(", ")

            for (instructor in instructors) {
                val result = query("""MATCH (i:Instructor) WHERE i.name = "$instructor" return i""")
                val record = result.next()[0].asMap()
                val databaseInstructor = Instructor(id = record["id"] as String, name = record["name"] as String)
                query("""
                MATCH (course:Course {id: "${course.id}"}), (instructor:Instructor {id: "${databaseInstructor.id}"})
                CREATE (course)-[:INSTRUCTED_BY]->(instructor)
                """.trimIndent())
            }

            val subjects = record[5].split(", ").map {
                if(it.substring(0..3) == "and ") {
                    it.substring(4)
                } else {
                    it
                }
            }

            for (subject in subjects) {
                val result = query("""MATCH (s:Subject) WHERE s.name = "$subject" RETURN s""")
                val record = result.next()[0].asMap()
                val databaseSubject = Subject(name = record["name"] as String)
                query("""
                MATCH (course:Course {id: "${course.id}"}), (subject:Subject {name: "${databaseSubject.name}"})
                CREATE (course)-[:SUBJECT_OF]->(subject)
                """.trimIndent())
            }

            val result = query("""MATCH (d:Difficulty) WHERE d.name = "${difficulties[Random.nextInt(0,3)]}" RETURN d """)
            val diffRecord = result.next()[0].asMap()
            val randomDifficulty = Difficulty(name = diffRecord["name"] as String)

            query("""
                MATCH (course:Course {id: "${course.id}"}), (difficulty:Difficulty {name: "${randomDifficulty.name}"})
                CREATE (course)-[:HAS_LEVEL]->(difficulty)
                """.trimIndent())


            val institution = record[0]

            val institutionResult = query("""MATCH (i:Institution) WHERE i.name = "$institution" RETURN i""")
            val institutionRecord = institutionResult.next()[0].asMap()
            val institutionFromNeo4j = Institution(id = institutionRecord["id"] as String, name = institutionRecord["name"] as String)

            query("""
                MATCH (course:Course {id: "${course.id}"}), (institution:Institution {id: "${institutionFromNeo4j.id}"})
                CREATE (course)-[:COURSE_AT]->(institution)
                """.trimIndent())
        }
    }
}