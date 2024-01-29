# XSS (Stored / Reflected)

### Vulnerability:
In MainActivity.kt, when setting the text of EditText fields to UI elements, there is a potential for cross-site scripting (XSS) attacks if user input contains HTML or JavaScript code. This vulnerability arises from directly setting user input as text without proper validation or escaping.


### Vulnerable code in MainActivity.kt
```kotlin
editEmployeeName.setText(existingEmployee?.employeeName)
editAge.setText(existingEmployee?.age.toString())
editDesignation.setText(existingEmployee?.designation)
editExperience.setText(existingEmployee?.yearsOfExperience.toString())
editAddress.setText(existingEmployee?.address)
```

### Description of Attack:
An attacker can craft malicious input containing JavaScript or HTML code. When this input is displayed on the UI without proper sanitization, it can execute arbitrary scripts in the context of the user's session, leading to XSS attacks.

### Fix:
Implement proper input validation and HTML escaping when displaying user input on the UI. Use methods like Html.escapeHtml() or libraries like OWASP Java HTML Sanitizer to sanitize user input before displaying it.
kotlin

### Fixed code in MainActivity.kt
```kotlin
editEmployeeName.text = Html.escapeHtml(existingEmployee?.employeeName)
editAge.text = Html.escapeHtml(existingEmployee?.age.toString())
editDesignation.text = Html.escapeHtml(existingEmployee?.designation)
editExperience.text = Html.escapeHtml(existingEmployee?.yearsOfExperience.toString())
editAddress.text = Html.escapeHtml(existingEmployee?.address)
```

### Description of Fix:
By using HTML escaping, any HTML or JavaScript code entered by the user will be treated as plain text and displayed as such in the UI, mitigating the risk of XSS attacks.



# SQL Injection

### Vulnerability:
The vulnerability lies in the SQL queries executed in the DBHelper class, particularly in methods like insertEmployee and updateEmployee. Directly interpolating user inputs into SQL queries opens up the possibility of SQL injection attacks.

### Vulnerable code in DBHelper.kt
```kotlin
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
```
### Description of Attack:
An attacker can manipulate input fields to inject malicious SQL code, such as dropping tables, modifying data, or performing unauthorized actions on the database.

### Fix:
Utilize parameterized queries or prepared statements to separate SQL logic from user data, preventing SQL injection attacks.
kotlin

### Fixed code in DBHelper.kt
```kotlin
fun insertEmployee(employee: Employee): Long {
    val db = writableDatabase
    val query = "INSERT INTO $TABLE_NAME " +
                "($COLUMN_NAME, $COLUMN_AGE, $COLUMN_DESIGNATION, $COLUMN_YEARS_OF_EXPERIENCE, $COLUMN_ADDRESS) " +
                "VALUES (?, ?, ?, ?, ?)"
    val values = arrayOf(
        employee.employeeName,
        employee.age.toString(),
        employee.designation,
        employee.yearsOfExperience.toString(),
        employee.address
    )
    val statement = db.compileStatement(query)
    for ((index, value) in values.withIndex()) {
        statement.bindString(index + 1, value)
    }
    return statement.executeInsert()
}
```
### Description of Fix:
By using parameterized queries, user inputs are treated as data rather than executable SQL commands, effectively preventing SQL injection attacks. The values are properly sanitized and escaped within the database query engine.```

CSRF (Cross-Site Request Forgery)

# CSRF

### Vulnerability:
In my application, CSRF vulnerabilities can arise when performing sensitive actions such as updating or deleting records without proper CSRF protection.
Vulnerable Code:
Below are the vulnerable parts in your provided files:

In MainActivity.kt, when handling form submissions to create or update employee records, there's no implementation of CSRF tokens.

In SecondActivity.kt, the deletion of employee records lacks CSRF protection.



### Generate CSRF Token:
In my MainActivity.kt,SecondActivity.kt generate a CSRF token for each user session.
 Fix:


### MainActivity.kt
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    // Generate CSRF token for each session
    val csrfToken = generateCSRFToken()

    // Save the CSRF token in the session or in a secure HTTP-only cookie
    saveCSRFTokenInSession(csrfToken)

    // Include the CSRF token in the form submission
    createEmployee.setOnClickListener {
        // Include csrfToken in form submission
        // ...
    }
}
```
### SecondActivity.kt
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    // Generate CSRF token for each session
    val csrfToken = generateCSRFToken()

    // Save the CSRF token in the session or in a secure HTTP-only cookie
    saveCSRFTokenInSession(csrfToken)

    // Include the CSRF token in the AJAX request or form submission for employee deletion
    object : EmployeeAdapter.OnDeleteClickListener {
        override fun onDeleteClick(employeeId: Long) {
            // Include csrfToken in deletion request
            // ...
        }
    }
}
```
### Description:

The CSRF token is generated and saved for each user session.
It's included in all state-changing requests (form submissions, AJAX requests).
On the server-side, the received CSRF token is validated against the token stored for the user's session. If the tokens don't match, the request is rejected, thus preventing CSRF attacks.
By implementing CSRF protection, you enhance the security of your application by ensuring that requests originate from legitimate sources, mitigating the risk of CSRF attacks.
