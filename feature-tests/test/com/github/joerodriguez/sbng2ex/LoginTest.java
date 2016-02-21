package com.github.joerodriguez.sbng2ex;

import org.fluentlenium.adapter.FluentTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(ExampleApiApplication.class)
@WebIntegrationTest("server.port:0")
public class LoginTest extends FluentTest {

    @Value("${local.server.port}")
    int port;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        DbTruncatorKt.DbTruncator(jdbcTemplate);

        jdbcTemplate.update(
                "INSERT INTO users (email, password) VALUES (?,?)",
                new Object[]{"tester@example.com", passwordEncoder.encode("tester-good-password")}
        );
    }

    @Test
    public void testLogin_withBadEmail() {
        goTo("http://localhost:" + port + "/");
        await().atMost(5, TimeUnit.SECONDS).until("body").containsText("Login");

        fill("#email").with("tester-unknown@example.com");
        fill("#password").with("tester-good-password");

        findFirst("input[type=submit]").click();

        await().atMost(5, TimeUnit.SECONDS).until("body").containsText("Login failed");
    }

    @Test
    public void testLogin_withBadPassword() {
        goTo("http://localhost:" + port + "/");
        await().atMost(5, TimeUnit.SECONDS).until("body").containsText("Login");

        fill("#email").with("tester@example.com");
        fill("#password").with("tester-bad-password");

        findFirst("input[type=submit]").click();

        await().atMost(5, TimeUnit.SECONDS).until("body").containsText("Login failed");
    }

    @Test
    public void testLogin() {
        goTo("http://localhost:" + port + "/");
        await().atMost(5, TimeUnit.SECONDS).until("body").containsText("Login");

        fill("#email").with("tester@example.com");
        fill("#password").with("tester-good-password");

        click("input[type=submit]");

        await().atMost(5, TimeUnit.SECONDS).until("body").containsText("Login succeeded");
    }
}
