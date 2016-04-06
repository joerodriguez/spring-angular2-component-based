package com.github.joerodriguez.sbng2ex.users

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.sql.ResultSet
import javax.sql.DataSource

@Component
class UsersRepository

    @Autowired
    constructor(
            private val dataSource: DataSource,
            private val jdbcTemplate: NamedParameterJdbcTemplate,
            private val passwordEncoder: PasswordEncoder
    )

{

    private val rowMapper: (ResultSet, Int) -> User = { rs, rowNum -> User(rs.getLong("id"), rs.getString("email")) }

    fun create(email: String, password: String): User {

        val params = mapOf(
                "email" to email,
                "password" to passwordEncoder.encode(password)
        )

        val insertedUserId = SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id")
                .executeAndReturnKey(params)

        return find(insertedUserId.toLong())
    }

    fun findByEmail(email: String): User? {

        val users = jdbcTemplate.query(
                "SELECT id, email FROM users WHERE email = :email",
                hashMapOf("email" to email),
                rowMapper
        )

        return if (users.isEmpty()) null else users[0]
    }

    private fun find(id: Long): User {

        return jdbcTemplate.queryForObject(
                "SELECT id, email FROM users WHERE id = :id",
                hashMapOf("id" to id),
                rowMapper
        )
    }

}
