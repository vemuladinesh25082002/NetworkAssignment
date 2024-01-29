// MainActivity.kt (Updated)
package com.example.myapplication_employee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var createEmployee: Button
    private lateinit var editEmployeeName: EditText
    private lateinit var editAge: EditText
    private lateinit var editDesignation: EditText
    private lateinit var editExperience: EditText
    private lateinit var editAddress: EditText
    private lateinit var dataSource: EmployeeDataSource
   private lateinit var dbHelper: DBHelper
   var isEditing=false
    var employeeId: Long=1
    override fun onCreate(savedInstanceState: Bundle?) {
        isEditing=false
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dataSource = EmployeeDataSource(this)
        dbHelper=DBHelper(this)
        createEmployee = findViewById(R.id.btn_create)
        editEmployeeName = findViewById(R.id.et_employeename)
        editAge = findViewById(R.id.et_Age)
        editDesignation = findViewById(R.id.et_Designation)
        editExperience = findViewById(R.id.et_Experience)
        editAddress = findViewById(R.id.et_Adress)

        editAge.inputType = InputType.TYPE_CLASS_NUMBER
        editExperience.inputType = InputType.TYPE_CLASS_NUMBER

        if (intent.hasExtra("edit_employee_id")) {
            isEditing = true
            employeeId = intent.getLongExtra("edit_employee_id", 1)

            // Fetch existing details based on employeeId
            val existingEmployee = dbHelper.getEmployeeById(employeeId)

            // Set existing details to the UI fields
            editEmployeeName.setText(existingEmployee?.employeeName)
            editAge.setText(existingEmployee?.age.toString())
            editDesignation.setText(existingEmployee?.designation)
            editExperience.setText(existingEmployee?.yearsOfExperience.toString())
            editAddress.setText(existingEmployee?.address)
        }
        createEmployee.setOnClickListener {
            if (isEditing) {
                // Update details in the database
                val updatedEmployee = Employee(
                    employeeId = employeeId,
                    employeeName = editEmployeeName.text.toString(),
                    age = editAge.text.toString().toInt(),
                    designation = editDesignation.text.toString(),
                    yearsOfExperience = editExperience.text.toString().toInt(),
                    address = editAddress.text.toString()
                )

                dbHelper.updateEmployee(updatedEmployee)

                val intent = Intent(this, SecondActivity::class.java)
                startActivity(intent)
                finish()

            }
            else
            {
                val employee = Employee(
                    employeeName=editEmployeeName.text.toString(),
                    age=editAge.text.toString().toInt(),
                    designation=editDesignation.text.toString(),
                    yearsOfExperience = editExperience.text.toString().toInt(),
                    address=editAddress.text.toString()
                )
                val id = dataSource.insertEmployee(employee)
                Toast.makeText(this, "Employee added with ID: $id", Toast.LENGTH_SHORT).show()

                // Start SecondActivity after adding the employee
                startActivity(Intent(this, SecondActivity::class.java))
            }
            }
    }


}
