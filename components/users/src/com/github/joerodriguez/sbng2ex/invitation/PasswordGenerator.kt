package com.github.joerodriguez.sbng2ex.invitation

import org.springframework.stereotype.Component

@Component
open class PasswordGenerator {
    open fun get(): String {
        return "test-password"
    }
}
