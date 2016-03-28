package requests

import com.fasterxml.jackson.databind.Module
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.joerodriguez.sbng2ex.TestDataSource
import com.github.joerodriguez.sbng2ex.UsersRepository
import com.github.joerodriguez.sbng2ex.invitation.InvitationRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.skyscreamer.jsonassert.JSONAssert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.TestRestTemplate
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import java.net.URI
import javax.sql.DataSource

@RunWith(SpringJUnit4ClassRunner::class)
@SpringApplicationConfiguration(TestContext::class)
@WebIntegrationTest("server.port:0")
class CreateInvitationRequestTest {

    @Value("\${local.server.port}")
    var port = 0;

    val endpoint: (String) -> URI = { path -> URI("http://localhost:$port$path") }

    val restTemplate = TestRestTemplate()

    @Autowired
    lateinit var usersRepository: UsersRepository

    @Test
    fun testSuccess() {
        val location = restTemplate.postForLocation(endpoint("/api/user-invitations"), InvitationRequest("john@aol.com"))


        val (id) = usersRepository.findByEmail("john@aol.com")!!
        assertThat(location).isEqualTo(endpoint("/api/users/$id"))
    }

    @Test
    fun testInvalidEmail() {
        val entity = restTemplate.postForEntity(endpoint("/api/user-invitations"), InvitationRequest("jackson_at_aol_com"), String::class.java)


        assertThat(entity.statusCode).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)

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
        JSONAssert.assertEquals(expected, entity.body, true);
    }

    @Test
    fun testEmailTaken() {
        usersRepository.create("jackson@aol.com", "_")


        val entity = restTemplate.postForEntity(endpoint("/api/user-invitations"), InvitationRequest("jackson@aol.com"), String::class.java)


        assertThat(entity.statusCode).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)

        val expected = """
            {
                errors: [
                    {
                        code: "email_taken",
                        message: "Email address has already been taken",
                        fieldName: "email"
                    }
                ]
            }
        """
        JSONAssert.assertEquals(expected, entity.body, true);
    }

}

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

