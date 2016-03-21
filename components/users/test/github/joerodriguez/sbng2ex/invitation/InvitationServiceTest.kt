package github.joerodriguez.sbng2ex.invitation

import com.github.joerodriguez.sbng2ex.User
import com.github.joerodriguez.sbng2ex.UserService
import com.github.joerodriguez.sbng2ex.invitation.EmailService
import com.github.joerodriguez.sbng2ex.invitation.InvitationRequest
import com.github.joerodriguez.sbng2ex.invitation.InvitationService
import com.github.joerodriguez.sbng2ex.invitation.PasswordGenerator
import com.github.joerodriguez.sbng2ex.service.ErrorType
import com.github.joerodriguez.sbng2ex.service.ServiceError
import com.github.joerodriguez.sbng2ex.service.ServiceResponse
import com.github.joerodriguez.sbng2ex.testhelper.stub
import com.github.joerodriguez.sbng2ex.testhelper.with
import com.github.joerodriguez.sbng2ex.testhelper.withException
import com.github.joerodriguez.sbng2ex.transaction.TransactionsFake
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito.mock
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
@PrepareForTest(EmailService::class, UserService::class, PasswordGenerator::class)
class InvitationServiceTest {

    val emailService = mock(EmailService::class.java);
    val userService = mock(UserService::class.java);
    val passwordGenerator = mock(PasswordGenerator::class.java);
    val transactions = TransactionsFake.create()

    val invitationService = InvitationService(
            transactions,
            passwordGenerator,
            emailService,
            userService
    )

    @Test
    fun testUserIsCreatedAndNotified() {
        stub(passwordGenerator.get()).with("funkyFresh")

        stub(userService.create("testuser@example.com", "funkyFresh"))
                .with(ServiceResponse.success(User(1, "testuser@example.com")))

        stub(emailService.sendInvitation("testuser@example.com", "funkyFresh"))
                .with(ServiceResponse.success<Any?>(null))


        val response = invitationService.invite(InvitationRequest("testuser@example.com"))


        assertThat(transactions.success()).isTrue()
        assertThat(response.isSuccess).isTrue()
        Mockito.verify(emailService).sendInvitation("testuser@example.com", "funkyFresh")
    }

    @Test
    fun testUserIsNotCreated_whenEmailIsTaken() {
        stub(passwordGenerator.get()).with("funkyFresh")

        stub(emailService.sendInvitation("testuser@example.com", "funkyFresh"))
                .with(ServiceResponse.success<Any?>(null))

        stub(userService.create("testuser@example.com", "funkyFresh"))
                .with(ServiceResponse.create<User> {
                    it.addError(ServiceError.create(ErrorType.EMAIL_TAKEN, "email"))
                })


        val response = invitationService.invite(InvitationRequest("testuser@example.com"))


        assertThat(transactions.didRollback()).isTrue()
        assertThat(response.isSuccess).isFalse()
        Mockito.verify(emailService).sendInvitation("testuser@example.com", "funkyFresh")
    }

    @Test
    fun testUserIsNotCreated_whenUserServiceThrows() {
        stub(passwordGenerator.get()).with("funkyFresh")

        stub(userService.create("testuser@example.com", "funkyFresh"))
                .withException(RuntimeException("duplicate key"))


        val response = invitationService.invite(InvitationRequest("testuser@example.com"))


        assertThat(transactions.didRollback()).isTrue()
        assertThat(response.isSuccess).isFalse()

        Mockito.verify(emailService, Mockito.never()).sendInvitation("testuser@example.com", "funkyFresh")
    }
}
