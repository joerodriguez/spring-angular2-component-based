package com.github.joerodriguez.sbng2ex.invitation;

public class InvitationRequest {
    private String email;

    public InvitationRequest(String email) {
        this.email = email;
    }

    public InvitationRequest() {
    }

    public String getEmail() {
        return email;
    }
}
