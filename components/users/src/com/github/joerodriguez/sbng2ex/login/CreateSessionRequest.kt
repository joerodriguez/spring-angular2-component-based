package com.github.joerodriguez.sbng2ex.login

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateSessionRequest(
        @JsonProperty("email") val email: String,
        @JsonProperty("password") val password: String
)
