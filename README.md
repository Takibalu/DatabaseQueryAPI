# DatabaseQueryAPI

A Spring Boot API that executes SQL queries from `.sql` files in a local `/queries` directory. It connects to a PostgreSQL database and returns results in JSON format.

---

## Features

-  Execute SQL queries by identifier via HTTP GET requests
-  Reads queries from `.sql` files (e.g. `queries/get_users.sql`)
-  Connects to a PostgreSQL database using Spring Data
-  Returns query results in JSON
-  Error handling for missing files or SQL errors

---

##  Technologies Used

- Java 17
- Spring Boot
- Spring Web
- Spring JDBC
- PostgreSQL
- JUnit (for testing)

---

### Installation and init

1. Clone the repository:

   ```bash
   git clone https://github.com/Takibalu/DatabaseQueryAPI.git

2. Configure your database connection in src/main/resources/application.properties:
```
   spring.datasource.url=jdbc:postgresql:{your_database_url}  
   spring.datasource.username={your_db_username} 
   spring.datasource.password={your_db_password}
```
3. Place your SQL query files (.sql) in the /queries directory.

---

## Usage

### API Endpoint

GET /execute-query?query_identifier={queryName}

- {queryName} — the name of the SQL file without the .sql extension.

### Example

Assuming you have a query file get_user_data.sql in the query directory:

SELECT id, name FROM users;

Request:

GET /execute-query?query_identifier=get_user_data

Response:

 ```
 {
    "success": true,
    "data": [
        {
            "id": 1,
            "name": "Alice"
        },
        {
            "id": 2,
            "name": "Bob"
        }
    ],
    "execution_time_ms": 1
}
 ```

---

## Error Handling

- If the query file is not found, the API returns 404 Not Found.
- If the SQL execution fails, the API returns 500 Internal Server Error with a descriptive message.

---

## Contact

For questions or feedback, please contact Takács Balázs - takibalu18@gmail.com.
