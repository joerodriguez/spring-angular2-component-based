package com.github.joerodriguez.sbng2ex.invitation;

import com.github.joerodriguez.sbng2ex.service.ServiceResponse;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class EmailService {
    private final Logger logger = getLogger(this.getClass());

    // TODO: This should really create an outbound email record in some table
    public ServiceResponse sendInvitation(String email, String password) {
        logger.debug("User created: " + email + " password: " + password);

        return ServiceResponse.success(null);
    }
}
