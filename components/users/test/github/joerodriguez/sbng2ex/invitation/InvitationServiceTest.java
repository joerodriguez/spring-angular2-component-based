package github.joerodriguez.sbng2ex.invitation;

import com.github.joerodriguez.sbng2ex.ServiceResponse;
import com.github.joerodriguez.sbng2ex.TestDataSource;
import com.github.joerodriguez.sbng2ex.invitation.EmailService;
import com.github.joerodriguez.sbng2ex.invitation.InvitationRequest;
import com.github.joerodriguez.sbng2ex.invitation.InvitationService;
import com.github.joerodriguez.sbng2ex.invitation.PasswordGenerator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class InvitationServiceTest {

    @Mock
    EmailService emailService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    PasswordGenerator passwordGenerator;

    JdbcTemplate jdbcTemplate;

    InvitationService invitationService;

    @Before
    public void setUp() throws Exception {
        TestDataSource testDataSource = new TestDataSource("spring-ng-example-test");
        jdbcTemplate = testDataSource.getJdbcTemplate();

        MockitoAnnotations.initMocks(this);

        invitationService = new InvitationService(
                testDataSource.getNamedParameterJdbcTemplate(),
                passwordEncoder,
                passwordGenerator,
                emailService
        );
    }

    @Test
    public void testUserIsCreatedAndNotified() {
        doReturn("funkyFresh").when(passwordGenerator).get();
        doReturn("funkyFresh-encoded").when(passwordEncoder).encode("funkyFresh");


        ServiceResponse<Void> response = invitationService.invite(new InvitationRequest("testuser@example.com"));


        assertThat(response.isSuccess(), equalTo(true));
        verify(emailService).sendInvitation(eq("testuser@example.com"), eq("funkyFresh"));

        Integer count = jdbcTemplate.queryForObject(
                "SELECT count(*) FROM users WHERE email = ? AND password = ?",
                new Object[]{"testuser@example.com", "funkyFresh-encoded"},
                Integer.class
        );

        assertThat(count, equalTo(1));
    }
}