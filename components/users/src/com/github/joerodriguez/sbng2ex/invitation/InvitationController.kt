package com.github.joerodriguez.sbng2ex.invitation

import com.github.joerodriguez.sbng2ex.web.Responders
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.composed.web.rest.json.PostJson
import org.springframework.http.HttpEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class InvitationController

    @Autowired
    constructor(
        private val invitationService: InvitationService,
        private val responders: Responders
    )

{

    @PostJson("/api/user-invitations")
    fun createInvitation(
            @RequestBody invitationRequest: InvitationRequest
    ): HttpEntity<Void> {
        val response = invitationService.invite(invitationRequest)

        return responders.post(response, { invitation -> "/api/users/" + invitation.userId })
    }

}
