/*
 * school.schema.1.sql

 * This class is generated by Kripton Annotation Processor (1.6.2-SNAPSHOT)</strong></p>
 * 
 * since Sun Jul 30 01:09:28 CEST 2017
 */
CREATE TABLE student (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, location TEXT);
CREATE TABLE seminar_2_student (id INTEGER PRIMARY KEY AUTOINCREMENT, student_id INTEGER, seminar_id INTEGER);
CREATE TABLE seminar (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, location TEXT);
CREATE TABLE professor (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, birth_date TEXT);
