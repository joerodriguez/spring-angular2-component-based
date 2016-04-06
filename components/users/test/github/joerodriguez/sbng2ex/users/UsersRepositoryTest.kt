package github.joerodriguez.sbng2ex.users

import com.github.joerodriguez.sbng2ex.dbtestinglibrary.TestDataSource
import com.github.joerodriguez.sbng2ex.users.UsersRepository
import io.damo.kspec.Spec
import org.assertj.core.api.Assertions.assertThat
import org.mockito.Mockito.mock
import org.mockito.Mockito.reset
import org.springframework.security.crypto.password.PasswordEncoder


class UsersRepositoryTest : Spec({

    val passwordEncoder = mock(PasswordEncoder::class.java)
    val testDataSource = TestDataSource("spring-ng2-example-test")
    val jdbcTemplate = testDataSource.jdbcTemplate

    val usersRepository: UsersRepository = UsersRepository(
            jdbcTemplate.dataSource,
            testDataSource.namedParameterJdbcTemplate,
            passwordEncoder
    )

    after {
        reset(passwordEncoder)
    }

    describe("#create") {
        test("create user with email and id") {
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

})
