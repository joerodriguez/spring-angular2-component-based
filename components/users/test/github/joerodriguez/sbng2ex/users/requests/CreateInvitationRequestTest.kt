package github.joerodriguez.sbng2ex.users.requests

import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.joerodriguez.sbng2ex.dbtestinglibrary.TestDataSource
import com.github.joerodriguez.sbng2ex.users.UsersRepository
import com.mashape.unirest.http.Unirest
import io.damo.kspec.spring.SpringSpec
import org.assertj.core.api.Assertions.assertThat
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import javax.sql.DataSource

@SpringApplicationConfiguration(TestContext::class)
@WebIntegrationTest("server.port:0")
class CreateInvitationRequestTest : SpringSpec({

    val port = injectValue("local.server.port", String::class)

    val endpoint: (String) -> String = { path -> "http://localhost:$port$path" }

    val usersRepository = inject(UsersRepository::class)

    describe("#create") {

        test("with a successful email") {
            val response = Unirest
                    .post(endpoint("/api/user-invitations"))
                    .header("Content-Type", "application/json")
                    .body(""" { "email": "john@aol.com" } """)
                    .asString()

            assertThat(response.status).isEqualTo(HttpStatus.CREATED.value())

            val (id) = usersRepository.findByEmail("john@aol.com")!!
            val expected = """
                                    {
                                        userId: $id
                                    }
                                """
            JSONAssert.assertEquals(expected, response.body, true);
        }

        test("with an invalid email") {
            val response = Unirest
                    .post(endpoint("/api/user-invitations"))
                    .header("Content-Type", "application/json")
                    .body(""" { "email": "invalid_email" } """)
                    .asString()


            assertThat(response.status).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value())

            val expected = """
                            {
                                errors: [
                                    {
                                        code: "email_invalid",
                                        message: "Email address is invalid",
                                        fieldName: "email"
                                    }
                                ]
                            }
                        """
            JSONAssert.assertEquals(expected, response.body, true);
        }

        test("with an email that already exists") {
            usersRepository.create("jackson@aol.com", "_")


            val response = Unirest
                    .post(endpoint("/api/user-invitations"))
                    .header("Content-Type", "application/json")
                    .body(""" { "email": "jackson@aol.com" } """)
                    .asString()


            assertThat(response.status).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value())

            val expected = """
                            {
                                errors: [
                                    {
                                        code: "email_taken",
                                        fieldName: "email",
                                        message: "Email address has already been taken"
                                    }
                                ]
                            }
                        """
            JSONAssert.assertEquals(expected, response.body, true);
        }
    }
})

@SpringBootApplication(scanBasePackages = arrayOf("com.github.joerodriguez.sbng2ex"))
open class TestContext {

    private val testDataSource = TestDataSource("spring-ng2-example-test")

    @Bean
    open fun getDataSource(): DataSource {
        return testDataSource.dataSource
    }

    @Bean
    open fun getJdbcTemplate(): JdbcTemplate {
        return testDataSource.jdbcTemplate
    }

    @Bean
    open fun getNamedParameterJdbcTemplate(): NamedParameterJdbcTemplate {
        return testDataSource.namedParameterJdbcTemplate
    }

    @Bean
    open fun getPasswordEncoder(): PasswordEncoder {
        return NoOpPasswordEncoder.getInstance()
    }

    @Bean
    open fun getAuthenticationManager(): AuthenticationManager {
        return object : AuthenticationManager {
            override fun authenticate(authentication: Authentication?): Authentication? {
                throw UnsupportedOperationException()
            }
        }
    }

    @Bean
    open fun parameterNamesModule(): Module {
        return KotlinModule()
    }

}
