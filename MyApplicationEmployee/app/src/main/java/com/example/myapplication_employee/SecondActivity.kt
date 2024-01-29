
package com.example.myapplication_employee

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SecondActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataSource: EmployeeDataSource
    private lateinit var dbHelper: DBHelper
    private lateinit var adapter: EmployeeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        dataSource = EmployeeDataSource(this)
        recyclerView = findViewById(R.id.recyclerView)

        // Set up RecyclerView
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        // Retrieve all employees from the database
        var employees = dataSource.getAllEmployees()
        //dataSource.deleteAllData()
        // Create and set the adapter for the RecyclerView
         adapter = EmployeeAdapter(employees,
            object : EmployeeAdapter.OnEditClickListener {
                override fun onEditClick(employeeId: Long) {
                    val intent = Intent(this@SecondActivity, MainActivity::class.java)
                    intent.putExtra("edit_employee_id", employeeId)
                    startActivity(intent)
                }
            },
            object : EmployeeAdapter.OnDeleteClickListener {
                override fun onDeleteClick(employeeId: Long) {

                    dbHelper.removeEmployee(employeeId)

                    adapter.updateData(dbHelper.getAllEmployees())
                }
            }
         )

        recyclerView.adapter = adapter
    }

//    override fun onResume() {
//        super.onResume()
//        adapter.updateData(dbHelper.getAllEmployees())
//    }

}
