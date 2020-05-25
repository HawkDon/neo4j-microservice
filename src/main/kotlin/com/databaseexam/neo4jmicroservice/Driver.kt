package com.databaseexam.neo4jmicroservice

import com.opencsv.CSVReader
import org.neo4j.driver.AuthToken
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.GraphDatabase
import org.neo4j.driver.TransactionWork
import org.springframework.stereotype.Component
import java.io.FileReader
import javax.annotation.PostConstruct

@Component
class Driver(private val populateData: PopulateData) {


    @PostConstruct
    fun initIt() {
        val driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "password"))
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
        populateData.populateNodesFromCSV(records, driver)
        // Create instructor relationships
        populateData.createInstructorRelationShips(records, driver)
        // Create course relationships
        populateData.createCourseRelationShips(records, driver)

    }
}