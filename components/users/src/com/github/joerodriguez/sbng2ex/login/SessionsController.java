package com.github.joerodriguez.sbng2ex.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("sessions")
public class SessionsController {

    @Autowired
    AuthenticationManager authenticationManager;

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<Void> create(
            @RequestBody CreateSessionRequest newSessionRequest
    ) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                newSessionRequest.getEmail(),
                newSessionRequest.getPassword()
        );
        CurrentUser currentUser = new CurrentUser(newSessionRequest.getEmail());
        token.setDetails(currentUser);

        try {
            Authentication auth = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
    }

}
