package com.github.joerodriguez.sbng2ex.invitation;

import com.github.joerodriguez.sbng2ex.User;
import com.github.joerodriguez.sbng2ex.UserService;
import com.github.joerodriguez.sbng2ex.service.ServiceResponse;
import com.github.joerodriguez.sbng2ex.transaction.Transactions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InvitationService {

    private final Transactions transactions;
    private final PasswordGenerator passwordGenerator;
    private final EmailService emailService;
    private final UserService userService;

    @Autowired
    public InvitationService(
            Transactions transactions,
            PasswordGenerator passwordGenerator,
            EmailService emailService,
            UserService userService
    ) {
        this.transactions = transactions;
        this.passwordGenerator = passwordGenerator;
        this.emailService = emailService;
        this.userService = userService;
    }

    public ServiceResponse<User> invite(InvitationRequest invitationRequest) {
        String password = passwordGenerator.get();

        return transactions.create(t -> {
            t.addWithEntity(userService.create(invitationRequest.getEmail(), password));
            t.add(emailService.sendInvitation(invitationRequest.getEmail(), password));
        });
    }

}
