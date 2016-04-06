package github.joerodriguez.sbng2ex.users.invitation

import com.github.joerodriguez.sbng2ex.users.User
import com.github.joerodriguez.sbng2ex.users.UserService
import com.github.joerodriguez.sbng2ex.users.invitation.EmailService
import com.github.joerodriguez.sbng2ex.users.invitation.InvitationRequest
import com.github.joerodriguez.sbng2ex.users.invitation.InvitationService
import com.github.joerodriguez.sbng2ex.users.invitation.PasswordGenerator
import com.github.joerodriguez.sbng2ex.servicestandard.service.ErrorType
import com.github.joerodriguez.sbng2ex.servicestandard.service.ServiceError
import com.github.joerodriguez.sbng2ex.servicestandard.service.ServiceResponse
import com.github.joerodriguez.sbng2ex.servicestandard.transaction.TransactionsFake
import github.joerodriguez.sbng2ex.users.stub
import github.joerodriguez.sbng2ex.users.with
import github.joerodriguez.sbng2ex.users.withException
import io.damo.kspec.Spec
import org.assertj.core.api.Assertions.assertThat
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.reset


class InvitationServiceTest : Spec({

    val emailService = mock(EmailService::class.java);
    val userService = mock(UserService::class.java);
    val passwordGenerator = mock(PasswordGenerator::class.java);
    var transactions = TransactionsFake.create()

    var invitationService = InvitationService(
            transactions,
            passwordGenerator,
            emailService,
            userService
    )

    after {
        reset(emailService)
        reset(userService)
        reset(passwordGenerator)
        transactions.reset()
    }

    describe("#invite") {
        test("when email is taken user is NOT notified") {
            stub(passwordGenerator.get()).with("funkyFresh")

            stub(emailService.sendInvitation("testuser@example.com", "funkyFresh"))
                    .with(ServiceResponse.success<Any?>(null))

            stub(userService.create("testuser@example.com", "funkyFresh"))
                    .with(ServiceResponse.create<User> {
                        it.error(ServiceError.create(ErrorType.EMAIL_TAKEN, "email"))
                    })

            val response = invitationService.invite(InvitationRequest("testuser@example.com"))

            assertThat(transactions.didRollback()).isTrue()
            assertThat(response.isSuccess).isFalse()
            Mockito.verify(emailService).sendInvitation("testuser@example.com", "funkyFresh")
        }

        test("user notified upon invitation") {
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

        test("when email is admin user is NOT created") {
            stub(passwordGenerator.get()).with("funkyFresh")

            stub(emailService.sendInvitation("admin", "funkyFresh"))
                    .with(ServiceResponse.success<Any?>(null))

            stub(userService.create("admin", "funkyFresh"))
                    .with(ServiceResponse.success(User(1, "testuser@example.com")))

            val response = invitationService.invite(InvitationRequest("admin"))

            assertThat(transactions.didRollback()).isTrue()
            assertThat(response.isSuccess).isFalse()
            assertThat(response.errors[0].code).isEqualTo("illegal_access")
        }

        test("when exception occurs user is NOT created") {
            stub(passwordGenerator.get()).with("funkyFresh")

            stub(userService.create("testuser@example.com", "funkyFresh"))
                    .withException(RuntimeException("duplicate key"))

            val response = invitationService.invite(InvitationRequest("testuser@example.com"))

            assertThat(transactions.didRollback()).isTrue()
            assertThat(response.isSuccess).isFalse()

            Mockito.verify(emailService, Mockito.never()).sendInvitation("testuser@example.com", "funkyFresh")
        }
    }
})
