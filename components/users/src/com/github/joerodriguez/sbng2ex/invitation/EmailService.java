package com.github.joerodriguez.sbng2ex.invitation;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;

@Component
public class EmailService {
    private final Logger logger = getLogger(this.getClass());

    public void sendInvitation(String email, String password) {
        logger.debug("User created: " + email + " password: " + password);
    }
}
