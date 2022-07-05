package com.nobelti.users;

import com.nobelti.users.model.Gender;
import com.nobelti.users.model.Role;
import com.nobelti.users.model.State;
import com.nobelti.users.model.User;
import com.nobelti.users.service.RoleService;
import com.nobelti.users.service.StateService;
import com.nobelti.users.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;


@SpringBootApplication
public class UsersApplication {

    public static void main(String[] args) {
        SpringApplication.run(UsersApplication.class, args);
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner run(UserService userService, RoleService roleService, StateService stateService) {
        return args -> {
            stateService.insertState(new State(null, "ACTIVATED"));
            stateService.insertState(new State(null, "INACTIVATED"));

            State stateActivated = stateService.findByState("ACTIVATED");

            roleService.insertRole(new Role(null, "ROLE_USER"));
            roleService.insertRole(new Role(null, "ROLE_ADMIN"));

            Role roleUser = roleService.findByRole("ROLE_USER");
            Role roleAdmin = roleService.findByRole("ROLE_ADMIN");

           userService.insertUser(new User(null,
                    "Bárbara",
                    "Terán",
                    19,
                    Gender.FEMALE,
                    "barbara@mail.com",
                    "1234",
                    stateActivated,
                    List.of(roleUser)
            ));

            userService.insertUser(new User(null,
                    "Jonayker",
                    "Rozo",
                    22,
                    Gender.MALE,
                    "jonayker@mail.com",
                    "1234",
                    stateActivated,
                    List.of(roleUser,roleAdmin)
            ));
        };
    }
}
