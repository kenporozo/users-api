package com.nobelti.users.service;

import com.nobelti.users.model.State;

public interface StateService {
    State insertState(State state);
    State findByState(String state);
}
