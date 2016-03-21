package com.github.joerodriguez.sbng2ex.invitation

import com.github.joerodriguez.sbng2ex.User
import com.github.joerodriguez.sbng2ex.UserService
import com.github.joerodriguez.sbng2ex.service.ServiceResponse
import com.github.joerodriguez.sbng2ex.transaction.Transactions
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

    fun invite(invitationRequest: InvitationRequest) = transactions.create<User> {
        val password = passwordGenerator.get()

        it.addWithEntity(userService.create(invitationRequest.email, password))
        it.add(emailService.sendInvitation(invitationRequest.email, password))
    }

}
