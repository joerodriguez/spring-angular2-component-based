package com.github.joerodriguez.sbng2ex.invitation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InvitationService {

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    PasswordGenerator passwordGenerator;

    @Autowired
    EmailService emailService;

    public ServiceResponse<Invitation> invite(InvitationRequest invitationRequest) {
        String password = passwordGenerator.get();

        Map<String, String> params = new HashMap<>();
        params.put("email", invitationRequest.getEmail());
        params.put("password", passwordEncoder.encode(password));


        namedParameterJdbcTemplate.update(
                "INSERT INTO users (email, password) VALUES (:email, :password)",
                params
        );

        emailService.sendInvitation(invitationRequest.getEmail(), password);

        return ServiceResponse.success();
    }
}
