package com.github.joerodriguez.sbng2ex.invitation

import com.github.joerodriguez.sbng2ex.service.ServiceResponse
import org.slf4j.LoggerFactory.getLogger
import org.springframework.stereotype.Component

@Component
open class EmailService {

    private val logger = getLogger(EmailService::class.java)

    // TODO: This should really create an outbound email record in some table
    open fun sendInvitation(email: String, password: String): ServiceResponse<Any?> {
        logger.debug("User created: $email password: $password")

        return ServiceResponse.success(null)
    }
}
