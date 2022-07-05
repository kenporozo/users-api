package com.nobelti.users.service;

import com.nobelti.users.model.State;
import com.nobelti.users.repository.StateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class StateServiceImpl implements StateService{
    private final StateRepository stateRepository;

    @Override
    public State insertState(State state) {
        log.info("Saving new state {}", state.getState());
        return stateRepository.saveAndFlush(state);
    }

    @Override
    public State findByState(String state) {
        log.info("Find by state = {}", state);
        return stateRepository.findByState(state);
    }
}
