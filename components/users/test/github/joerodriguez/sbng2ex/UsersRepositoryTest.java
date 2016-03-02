package github.joerodriguez.sbng2ex;

import com.github.joerodriguez.sbng2ex.TestDataSource;
import com.github.joerodriguez.sbng2ex.User;
import com.github.joerodriguez.sbng2ex.UsersRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;

public class UsersRepositoryTest {

    @Mock
    PasswordEncoder passwordEncoder;

    JdbcTemplate jdbcTemplate;

    UsersRepository usersRepository;

    @Before
    public void setUp() throws Exception {
        TestDataSource testDataSource = new TestDataSource("spring-ng-example-test");
        jdbcTemplate = testDataSource.getJdbcTemplate();

        MockitoAnnotations.initMocks(this);

        usersRepository = new UsersRepository(
                testDataSource.getJdbcTemplate(),
                passwordEncoder
        );
    }

    @Test
    public void testUserIsCreatedAndNotified() {
        doReturn("funkyFresh-encoded").when(passwordEncoder).encode("funkyFresh");


        User user = usersRepository.create("testuser@example.com", "funkyFresh");


        Long id = jdbcTemplate.queryForObject(
                "SELECT id FROM users WHERE email = ? AND password = ?",
                new Object[]{"testuser@example.com", "funkyFresh-encoded"},
                Long.class
        );

        assertThat(user.getEmail(), equalTo("testuser@example.com"));

        assertThat(id, equalTo(user.getId()));
    }
}