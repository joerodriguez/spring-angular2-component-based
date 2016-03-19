package com.github.joerodriguez.sbng2ex.login

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/sessions")
class SessionsController {

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @RequestMapping(method = arrayOf(RequestMethod.POST), consumes = arrayOf("application/json"))
    fun create(@RequestBody newSessionRequest: CreateSessionRequest): ResponseEntity<Void> {

        val token = UsernamePasswordAuthenticationToken(newSessionRequest.email, newSessionRequest.password)
        token.details = CurrentUser(newSessionRequest.email)

        try {
            val auth = authenticationManager.authenticate(token)
            SecurityContextHolder.getContext().authentication = auth

            return ResponseEntity(HttpStatus.CREATED)
        } catch (e: BadCredentialsException) {
            return ResponseEntity(HttpStatus.UNAUTHORIZED)
        }

    }

    @RequestMapping(method = arrayOf(RequestMethod.DELETE))
    fun delete(): ResponseEntity<Void> {
        SecurityContextHolder.getContext().authentication = null
        return ResponseEntity(HttpStatus.OK)
    }

}
