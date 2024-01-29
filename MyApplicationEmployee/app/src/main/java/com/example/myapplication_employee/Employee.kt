// Employee.kt
package com.example.myapplication_employee

import java.io.Serializable

data class Employee(
    val employeeId: Long=-1,
    val employeeName: String,
    val age: Int,
    val designation: String,
    val yearsOfExperience: Int,
    val address: String
):Serializable
