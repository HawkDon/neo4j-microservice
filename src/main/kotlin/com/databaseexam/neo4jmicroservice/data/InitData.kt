package com.databaseexam.neo4jmicroservice.data

import com.databaseexam.neo4jmicroservice.nodes.Course
import com.databaseexam.neo4jmicroservice.nodes.Institution
import com.databaseexam.neo4jmicroservice.nodes.Instructor
import com.databaseexam.neo4jmicroservice.nodes.Subject
import com.databaseexam.neo4jmicroservice.repositories.CourseRepository
import com.databaseexam.neo4jmicroservice.repositories.InstitutionRepository
import com.databaseexam.neo4jmicroservice.repositories.InstructorRepository
import com.databaseexam.neo4jmicroservice.repositories.SubjectRepository
import com.opencsv.CSVReader
import org.springframework.stereotype.Component
import java.io.FileReader
import javax.annotation.PostConstruct

@Component
class InitData(private val populateData: PopulateData) {

    @PostConstruct
    fun initIt() {

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

        // Create nodes
        populateData.populateNodesFromCSV(records)
        // Create instructor relationships
        populateData.createInstructorRelationShips(records)
        // Create course relationships
        populateData.createCourseRelationShips(records)
    }
}