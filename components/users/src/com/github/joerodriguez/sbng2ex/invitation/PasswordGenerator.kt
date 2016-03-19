package com.github.joerodriguez.sbng2ex.invitation

import org.springframework.stereotype.Component

@Component
class PasswordGenerator {
    fun get(): String {
        return "test-password"
    }
}
