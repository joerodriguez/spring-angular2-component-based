package com.github.joerodriguez.sbng2ex.users.invitation

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.joerodriguez.sbng2ex.users.UserService
import com.github.joerodriguez.sbng2ex.servicestandard.service.ErrorType
import com.github.joerodriguez.sbng2ex.servicestandard.transaction.Transactions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class InvitationService

    @Autowired
    constructor(
            private val transactions: Transactions,
            private val passwordGenerator: PasswordGenerator,
            private val emailService: EmailService,
            private val userService: UserService
    )

{

    fun invite(invitationRequest: InvitationRequest) = transactions.create<Invitation> {
        val password = passwordGenerator.get()
        val email = invitationRequest.email

        val userResponse = userService.create(email, password)

        if (email == "admin") {
            it.error(ErrorType.ILLEGAL_ACCESS.forField("email"))
        }

        it.add(userResponse)
        it.add(emailService.sendInvitation(email, password))

        it.apply { Invitation(userResponse.entity!!.id) }
    }

}

data class Invitation(val userId: Long)

data class InvitationRequest(
        @JsonProperty("email") val email: String
)
