package com.github.joerodriguez.sbng2ex;

import com.github.joerodriguez.sbng2ex.service.ErrorType;
import com.github.joerodriguez.sbng2ex.service.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class UserService {

    public static final Pattern EMAIL_PATTERN = Pattern.compile("^(.+)@(.+)$");

    private final UsersRepository usersRepository;

    @Autowired
    public UserService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public ServiceResponse<User> create(String email, String password) {
        return ServiceResponse.create(response -> {

            if (!EMAIL_PATTERN.matcher(email).matches()) {
                response.addError(ErrorType.INVALID_EMAIL.forField("email"));
            }

            if (usersRepository.findByEmail(email).isPresent()) {
                response.addError(ErrorType.EMAIL_TAKEN.forField("email"));
            }

            response.apply(() -> usersRepository.create(email, password));
        });
    }
}
