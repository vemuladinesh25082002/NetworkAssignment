// DBHelper.kt
package com.example.myapplication_employee

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "EmployeeDatabase"
        const val DATABASE_VERSION = 1

        const val TABLE_NAME = "EmployeeTable"
        const val COLUMN_ID = "EmployeeId"
        const val COLUMN_NAME = "EmployeeName"
        const val COLUMN_AGE = "Age"
        const val COLUMN_DESIGNATION = "Designation"
        const val COLUMN_YEARS_OF_EXPERIENCE = "YearsOfExperience"
        const val COLUMN_ADDRESS = "Address"
    }

    private val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
            "$COLUMN_ID INTEGER PRIMARY KEY, " +
            "$COLUMN_NAME TEXT, " +
            "$COLUMN_AGE INTEGER, " +
            "$COLUMN_DESIGNATION TEXT, " +
            "$COLUMN_YEARS_OF_EXPERIENCE INTEGER, " +
            "$COLUMN_ADDRESS TEXT);"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertEmployee(employee: Employee): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, employee.employeeName)
            put(COLUMN_AGE, employee.age)
            put(COLUMN_DESIGNATION, employee.designation)
            put(COLUMN_YEARS_OF_EXPERIENCE, employee.yearsOfExperience)
            put(COLUMN_ADDRESS, employee.address)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun getAllEmployees(): List<Employee> {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        return parseEmployees(cursor)
    }
    fun updateEmployee(employee: Employee): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, employee.employeeName)
            put(COLUMN_AGE, employee.age)
            put(COLUMN_DESIGNATION, employee.designation)
            put(COLUMN_YEARS_OF_EXPERIENCE, employee.yearsOfExperience)
            put(COLUMN_ADDRESS, employee.address)
        }
        return db.update(
            TABLE_NAME,
            values,
            "$COLUMN_ID = ?",
            arrayOf(employee.employeeId.toString())
        )
    }

    fun removeEmployee(employeeId: Long): Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(employeeId.toString()))
    }
    fun deleteAllData() {
        val db = this.writableDatabase
        db.delete("EmployeeTable", null, null)
        db.close()
    }

    private fun parseEmployees(cursor: Cursor): List<Employee> {
        val employees = mutableListOf<Employee>()
        while (cursor.moveToNext()) {
            val employee = Employee(
                cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESIGNATION)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_YEARS_OF_EXPERIENCE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))
            )
            employees.add(employee)
        }

        cursor.close()
        return employees
    }
    fun getEmployeeById(employeeId: Long): Employee? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_AGE,
                COLUMN_DESIGNATION,
                COLUMN_YEARS_OF_EXPERIENCE,
                COLUMN_ADDRESS
            ),
            "$COLUMN_ID = ?",
            arrayOf(employeeId.toString()),
            null,
            null,
            null,
            null
        )

        var employee: Employee? = null

        if (cursor.moveToFirst()) {
            employee = Employee(
                cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESIGNATION)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_YEARS_OF_EXPERIENCE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))
            )
        }

        cursor.close()
        return employee
    }
}
