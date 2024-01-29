// EmployeeDataSource.kt (Updated)
package com.example.myapplication_employee

import android.content.Context

class EmployeeDataSource(context: Context) {

    private val dbHelper: DBHelper = DBHelper(context)

    fun insertEmployee(employee: Employee): Long {
        return dbHelper.insertEmployee(employee)
    }

    fun getAllEmployees(): List<Employee> {
        return dbHelper.getAllEmployees()
    }
    fun deleteAllData() {
        dbHelper.deleteAllData()
    }
}
