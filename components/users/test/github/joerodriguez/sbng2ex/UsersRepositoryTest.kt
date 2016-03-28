package github.joerodriguez.sbng2ex

import com.github.joerodriguez.sbng2ex.TestDataSource
import com.github.joerodriguez.sbng2ex.UsersRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.Mockito.mock
import org.springframework.security.crypto.password.PasswordEncoder


class UsersRepositoryTest {

    val passwordEncoder = mock(PasswordEncoder::class.java)
    val testDataSource = TestDataSource("spring-ng2-example-test")
    val jdbcTemplate = testDataSource.jdbcTemplate

    val usersRepository: UsersRepository = UsersRepository(
            jdbcTemplate.dataSource,
            testDataSource.namedParameterJdbcTemplate,
            passwordEncoder
    )

    @Test
    fun testUserIsCreatedAndNotified() {
        stub(passwordEncoder.encode("funkyFresh")).with("funkyFresh-encoded")


        val user = usersRepository.create("testuser@example.com", "funkyFresh")


        val id = jdbcTemplate.queryForObject(
                "SELECT id FROM users WHERE email = ? AND password = ?",
                arrayOf("testuser@example.com", "funkyFresh-encoded"),
                Long::class.java
        )

        assertThat(user.email).isEqualTo("testuser@example.com")
        assertThat(id).isEqualTo(user.id)
    }
}