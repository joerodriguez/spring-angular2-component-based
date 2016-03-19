package com.github.joerodriguez.sbng2ex.invitation

import com.fasterxml.jackson.annotation.JsonProperty

data class InvitationRequest(
        @JsonProperty("email") val email: String
)
