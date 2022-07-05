package com.nobelti.users.repository;

import com.nobelti.users.model.Gender;
import com.nobelti.users.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByAge(Integer age);
    List<User> findByGender(Enum<Gender> gender);
    List<User> findByGenderAndAge(Enum<Gender> gender, Integer age);
    User findByEmail(String email);
    @Modifying
    @Query(
            value = "UPDATE User u SET u.state = 2 WHERE u.idUser = :id"
    )
    void deleteUserById(Long id);
}
