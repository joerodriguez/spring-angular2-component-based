package com.github.joerodriguez.sbng2ex.invitation

import com.github.joerodriguez.sbng2ex.web.Responders
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
class InvitationController

    @Autowired
    constructor(
        private val invitationService: InvitationService,
        private val responders: Responders
    )

{

    @RequestMapping(value = "/api/user-invitations", method = arrayOf(RequestMethod.POST), consumes = arrayOf("application/json"), produces = arrayOf("application/json"))
    fun createInvitation(
            @RequestBody invitationRequest: InvitationRequest
    ): HttpEntity<Void> {
        val response = invitationService.invite(invitationRequest)

        return responders.post(response, { invitation -> "/api/users/" + invitation.userId })
    }

}
