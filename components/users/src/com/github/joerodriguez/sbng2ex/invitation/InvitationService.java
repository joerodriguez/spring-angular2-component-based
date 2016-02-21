package com.github.joerodriguez.sbng2ex.invitation;

import com.github.joerodriguez.sbng2ex.ServiceResponse;
import com.github.joerodriguez.sbng2ex.User;
import com.github.joerodriguez.sbng2ex.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InvitationService {

    private final PasswordGenerator passwordGenerator;
    private final EmailService emailService;
    private final UsersRepository usersRepository;

    @Autowired
    public InvitationService(PasswordGenerator passwordGenerator, EmailService emailService, UsersRepository usersRepository) {
        this.passwordGenerator = passwordGenerator;
        this.emailService = emailService;
        this.usersRepository = usersRepository;
    }

    public ServiceResponse<User> invite(InvitationRequest invitationRequest) {
        String password = passwordGenerator.get();

        User user = usersRepository.create(invitationRequest.getEmail(), password);

        emailService.sendInvitation(invitationRequest.getEmail(), password);

        return ServiceResponse.success(user);
    }
}
