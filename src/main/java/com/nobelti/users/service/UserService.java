package com.nobelti.users.service;

import com.nobelti.users.exceptions.InvalidRequestException;
import com.nobelti.users.model.User;

import java.util.List;

public interface UserService {
    List<User> getUsers(String gender, Integer age);
    List<User> getUsersById(List<Long> ids) throws InvalidRequestException;
    User insertUser(User user) throws InvalidRequestException;
    void updateUser(User user) throws InvalidRequestException;
    void deleteUser(Long id) throws InvalidRequestException;
    void addRoleToUserById(Long id, String role) throws InvalidRequestException;
}
