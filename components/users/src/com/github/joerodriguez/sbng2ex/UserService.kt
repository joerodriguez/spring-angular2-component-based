package com.github.joerodriguez.sbng2ex

import com.github.joerodriguez.sbng2ex.service.ErrorType
import com.github.joerodriguez.sbng2ex.service.ServiceResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.util.regex.Pattern

@Component
class UserService

    @Autowired
    constructor(private val usersRepository: UsersRepository)

{

    private val emailPattern = Pattern.compile("^(.+)@(.+)$")

    fun create(email: String, password: String) = ServiceResponse.create<User> {

        if (!emailPattern.matcher(email).matches()) {
            it.error(ErrorType.INVALID_EMAIL.forField("email"))
        }

        if (usersRepository.findByEmail(email) != null) {
            it.error(ErrorType.EMAIL_TAKEN.forField("email"))
        }

        it.apply { usersRepository.create(email, password) }
    }

}
