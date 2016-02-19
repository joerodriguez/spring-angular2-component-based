package com.github.joerodriguez.sbng2ex.invitation;

import org.springframework.stereotype.Component;

@Component
public class PasswordGenerator {
    public String get() {
        return "test-password";
    }
}
