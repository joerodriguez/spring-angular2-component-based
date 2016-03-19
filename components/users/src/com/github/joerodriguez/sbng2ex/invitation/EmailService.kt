package com.github.joerodriguez.sbng2ex.invitation

import com.github.joerodriguez.sbng2ex.service.ServiceResponse
import org.slf4j.LoggerFactory
import org.slf4j.LoggerFactory.getLogger
import org.springframework.stereotype.Component
import java.util.logging.Logger

@Component
class EmailService {

    private val logger = LoggerFactory.getLogger(EmailService::class.java)

    // TODO: This should really create an outbound email record in some table
    fun sendInvitation(email: String, password: String): ServiceResponse<Any?> {
        logger.debug("User created: $email password: $password")

        return ServiceResponse.success(null)
    }
}
