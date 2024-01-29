// EmployeeAdapter.kt (Updated)
package com.example.myapplication_employee

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EmployeeAdapter(
    private var employees: List<Employee>,
    private val editClickListener: OnEditClickListener,
    private val deleteClickListener: OnDeleteClickListener
) : RecyclerView.Adapter<EmployeeAdapter.ViewHolder>() {

    interface OnEditClickListener {
        fun onEditClick(employeeId: Long)
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(employeeId: Long)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val employeeName: TextView = itemView.findViewById(R.id.tv_employeename)
        val employeeId: TextView = itemView.findViewById(R.id.tv_employeeid)
        val age: TextView = itemView.findViewById(R.id.tv_Age)
        val designation: TextView = itemView.findViewById(R.id.tv_Designation)
        val experience: TextView = itemView.findViewById(R.id.tv_Experience)
        val address: TextView = itemView.findViewById(R.id.tv_Adress)
        val editBtn: Button =itemView.findViewById(R.id.btn_edit)
        val deleteBtn: Button = itemView.findViewById(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.employee_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val employee = employees[position]
        holder.employeeName.text = "Employee Name: ${employee.employeeName}"
        holder.employeeId.text = "Employee ID: ${employee.employeeId}"
        holder.age.text = "Age: ${employee.age}"
        holder.designation.text = "Designation: ${employee.designation}"
        holder.experience.text = "Experience: ${employee.yearsOfExperience} years"
        holder.address.text = "Address: ${employee.address}"
        holder.editBtn.setOnClickListener {
            editClickListener.onEditClick(employee.employeeId)
        }

        holder.deleteBtn.setOnClickListener {
            deleteClickListener.onDeleteClick(employee.employeeId)
        }
    }
    fun updateData(newEmployees: List<Employee>)
    {
        employees=newEmployees
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return employees.size
    }
}
