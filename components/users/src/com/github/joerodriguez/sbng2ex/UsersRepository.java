package com.github.joerodriguez.sbng2ex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UsersRepository {

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;
    RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) -> new User(rs.getLong("id"), rs.getString("email"));

    @Autowired
    public UsersRepository(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    public User create(String email, String password) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", email);
        params.addValue("password", passwordEncoder.encode(password));

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        Number generatedId = simpleJdbcInsert.executeAndReturnKey(params);

        return find(generatedId.longValue());
    }

    public Optional<User> findByEmail(String email) {
        List<User> users = jdbcTemplate.query(
                "SELECT id, email FROM users WHERE email = ?",
                new Object[]{email},
                USER_ROW_MAPPER
        );

        return Optional.ofNullable(users.isEmpty() ? null : users.get(0));
    }

    private User find(long id) {
        return jdbcTemplate.queryForObject(
                "SELECT id, email FROM users WHERE id = ?",
                new Object[]{id},
                USER_ROW_MAPPER
        );
    }

}
