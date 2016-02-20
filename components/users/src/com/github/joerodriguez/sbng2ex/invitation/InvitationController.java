package com.github.joerodriguez.sbng2ex.invitation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InvitationController {

    @Autowired InvitationService invitationService;

    @RequestMapping(value = "/api//user-invitations", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity createInvitation(
            @RequestBody InvitationRequest invitationRequest
    ) {
        return respond(invitationService.invite(invitationRequest));
    }

    private ResponseEntity respond(ServiceResponse<Invitation> response) {
        return new ResponseEntity(response.isSuccess() ? HttpStatus.CREATED : HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
