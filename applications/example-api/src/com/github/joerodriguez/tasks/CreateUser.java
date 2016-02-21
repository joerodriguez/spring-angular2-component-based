package com.github.joerodriguez.tasks;

import com.github.joerodriguez.sbng2ex.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("createUser")
public class CreateUser implements AppTask {

    @Autowired
    UsersRepository usersRepository;

    @Override
    public void run() {
        usersRepository.create(System.getenv("EMAIL"), System.getenv("PASSWORD"));
    }
}
