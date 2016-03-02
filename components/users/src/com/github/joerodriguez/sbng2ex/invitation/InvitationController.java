package com.github.joerodriguez.sbng2ex.invitation;

import com.github.joerodriguez.sbng2ex.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.github.joerodriguez.sbng2ex.web.ServiceResponseWebResponder.respond;

@RestController
public class InvitationController {

    @Autowired
    InvitationService invitationService;

    @RequestMapping(
            value = "/api/user-invitations",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<User> createInvitation(
            @RequestBody InvitationRequest invitationRequest
    ) {
        return respond(invitationService.invite(invitationRequest), HttpStatus.CREATED, HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
