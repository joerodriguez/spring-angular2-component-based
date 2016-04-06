package com.github.joerodriguez.sbng2ex.feature

import com.github.joerodriguez.sbng2ex.ExampleApiApplication
import com.github.joerodriguez.sbng2ex.dbtestinglibrary.truncateDb
import io.damo.kspec.spring.SpringSpec
import net.codestory.simplelenium.FluentTest
import org.springframework.boot.test.SpringApplicationConfiguration
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles

@SpringApplicationConfiguration(ExampleApiApplication::class)
@WebIntegrationTest("server.port:0")
@ActiveProfiles("test")
class LoginTest : SpringSpec({

    val port = injectValue("local.server.port", Int::class)
    val passwordEncoder = inject(PasswordEncoder::class)
    val fluentTest = FluentTest()

    val jdbcTemplate = inject(JdbcTemplate::class)

    before {
        jdbcTemplate.truncateDb()
        jdbcTemplate.update(
                "INSERT INTO users (email, password) VALUES (?,?)",
                *arrayOf<Any>("tester@example.com", passwordEncoder.encode("tester-good-password")))
    }

    describe("#login") {
        test("when email is bad") {
            val form = fluentTest.goTo("http://localhost:$port/").find("#login")

            form.find("#email").fill("tester-unknown@example.com")
            form.find("#password").fill("tester-good-password")

            form.find("input[type=submit]").first().click()
            form.should().contain("Login failed")
        }

        test("when password is bad") {
            val form = fluentTest.goTo("http://localhost:$port/").find("#login")

            form.find("#email").fill("tester@example.com")
            form.find("#password").fill("tester-bad-password")

            form.find("input[type=submit]").first().click()
            form.should().contain("Login failed")
        }

        test("when successful") {
            val form = fluentTest.goTo("http://localhost:$port/").find("#login")

            form.find("#email").fill("tester@example.com")
            form.find("#password").fill("tester-good-password")

            form.find("input[type=submit]").first().click()
            form.should().contain("Login succeeded")
        }
    }
})
