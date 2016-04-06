package com.github.joerodriguez.sbng2ex.users.invitation

import org.springframework.stereotype.Component

@Component
open class PasswordGenerator {
    open fun get(): String {
        return "test-password"
    }
}
