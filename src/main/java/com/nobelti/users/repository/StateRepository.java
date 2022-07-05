package com.nobelti.users.repository;

import com.nobelti.users.model.State;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateRepository extends JpaRepository<State, Long> {
    State findByState(String state);
}
