package github.joerodriguez.sbng2ex.invitation;

import com.github.joerodriguez.sbng2ex.*;
import com.github.joerodriguez.sbng2ex.invitation.EmailService;
import com.github.joerodriguez.sbng2ex.invitation.InvitationRequest;
import com.github.joerodriguez.sbng2ex.invitation.InvitationService;
import com.github.joerodriguez.sbng2ex.invitation.PasswordGenerator;
import com.github.joerodriguez.sbng2ex.service.ErrorType;
import com.github.joerodriguez.sbng2ex.service.ServiceError;
import com.github.joerodriguez.sbng2ex.service.ServiceResponse;
import com.github.joerodriguez.sbng2ex.transaction.TransactionsFake;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class InvitationServiceTest {

    @Mock
    EmailService emailService;

    @Mock
    UserService userService;

    @Mock
    PasswordGenerator passwordGenerator;

    TransactionsFake transactions;

    InvitationService invitationService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        transactions = TransactionsFake.create();

        invitationService = new InvitationService(
                transactions,
                passwordGenerator,
                emailService,
                userService
        );
    }

    @Test
    public void testUserIsCreatedAndNotified() {
        doReturn("funkyFresh").when(passwordGenerator).get();
        doReturn(ServiceResponse.success(new User(1, "testuser@example.com"))).when(userService).create("testuser@example.com", "funkyFresh");
        doReturn(ServiceResponse.success(null)).when(emailService).sendInvitation("testuser@example.com", "funkyFresh");


        ServiceResponse<User> response = invitationService.invite(new InvitationRequest("testuser@example.com"));


        assertTrue(transactions.success());
        assertThat(response.isSuccess(), equalTo(true));
        verify(emailService).sendInvitation(eq("testuser@example.com"), eq("funkyFresh"));
    }

    @Test
    public void testUserIsNotCreated_whenEmailIsTaken() {
        doReturn("funkyFresh").when(passwordGenerator).get();
        doReturn(ServiceResponse.success(null)).when(emailService).sendInvitation("testuser@example.com", "funkyFresh");

        doReturn(
                ServiceResponse.create((t) -> t.addError(ServiceError.create(ErrorType.EMAIL_TAKEN, "email")))
        ).when(userService).create("testuser@example.com", "funkyFresh");


        ServiceResponse<User> response = invitationService.invite(new InvitationRequest("testuser@example.com"));


        assertTrue(transactions.didRollback());
        assertThat(response.isSuccess(), equalTo(false));
        verify(emailService).sendInvitation(eq("testuser@example.com"), eq("funkyFresh"));
    }

    @Test
    public void testUserIsNotCreated_whenUserServiceThrows() {
        doReturn("funkyFresh").when(passwordGenerator).get();

        doThrow(
                new RuntimeException("duplicate key")
        ).when(userService).create("testuser@example.com", "funkyFresh");


        ServiceResponse<User> response = invitationService.invite(new InvitationRequest("testuser@example.com"));


        assertTrue(transactions.didRollback());
        assertThat(response.isSuccess(), equalTo(false));
        assertThat(response.getErrors().get(0).getExtendedMessage(), equalTo("duplicate key"));
        verify(emailService, never()).sendInvitation(eq("testuser@example.com"), eq("funkyFresh"));
    }
}