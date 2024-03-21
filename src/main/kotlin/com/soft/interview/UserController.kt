package com.soft.interview

import org.springframework.jdbc.core.JdbcOperations
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@Service
@RestController("/user")
class UserController(
    val jdbcOperations: JdbcOperations
) {

    /**
     * Create users with default department
     */
    @GetMapping("/create")
    fun createUser(@RequestBody createUsersRequest: CreateUsersRequest) {
        createUsersRequest.users.forEach { user ->
            jdbcOperations.update("INSERT INTO users VALUES (${user.id}, '${user.name}', ${user.age}, '${user.jobTitle}')")
        }

        createUsersRequest.users.forEach { user ->
            jdbcOperations.update("INSERT INTO department_allocations VALUES (1, ${user.id})")
        }
    }

    @GetMapping("/by-department/{departmentId}")
    fun findAllByDepartment(@PathVariable departmentId: Int): List<User> =
        jdbcOperations.query(
            """
            SELECT u.*, d.* FROM users u
            JOIN department_allocations d ON d.user_id = u.id
            WHERE d.department_id = $departmentId
            """
        ) { rs, _ ->
            User(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4))
        }

    @GetMapping("/find/{name}")
    fun findUsersByName(@PathVariable name: String): List<User> =
        jdbcOperations.query("SELECT * FROM users WHERE name LIKE '%$name%'") { rs, _ ->
            User(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getString(4))
        }

}

data class CreateUsersRequest(val users: List<User>)

data class User(val id: Int, val name: String, val age: Int, val jobTitle: String)