package com.nobelti.users.controller;

import com.nobelti.users.exceptions.InvalidRequestException;
import com.nobelti.users.model.User;
import com.nobelti.users.model.User2Roles;
import com.nobelti.users.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers(@RequestParam(name = "gender",required = false) String gender,
                                               @RequestParam(name = "age", required = false) Integer age) throws InvalidRequestException {
        return ResponseEntity.ok(userService.getUsers(gender, age));
    }

    @GetMapping("/users/{ids}")
    public ResponseEntity<List<User>> getUsersById(@PathVariable(name = "ids") List<Long> ids) throws InvalidRequestException {
        return ResponseEntity.ok(userService.getUsersById(ids));
    }

    @PostMapping("/users")
    public ResponseEntity<User> insertUser(@RequestBody User user) throws InvalidRequestException {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/users").toUriString());
        return ResponseEntity.created(uri).body(userService.insertUser(user));
    }

    @PostMapping("/users/roles")
    public ResponseEntity<?> user2Roles(@RequestBody User2Roles user2Roles) throws InvalidRequestException{
        userService.addRoleToUserById(user2Roles.getIdUser(), user2Roles.getRoleName());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable(name = "id") Long id) throws InvalidRequestException{
        user.setIdUser(id);
        userService.updateUser(user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable(name = "id") Long id) throws InvalidRequestException {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}

