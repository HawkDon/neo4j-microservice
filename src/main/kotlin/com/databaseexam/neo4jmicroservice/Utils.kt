package com.databaseexam.neo4jmicroservice

fun getAllSubjects(records: MutableList<Array<String>>) = records.map {
    val subjects: MutableList<String> = it[5].split(", ") as MutableList<String>
    if(subjects[subjects.size - 1].substring(0..3) == "and ") {
        subjects[subjects.size - 1] = subjects[subjects.size - 1].substring(4);
    }
    subjects
}.flatten().toSet()