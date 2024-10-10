package dev.cloud.service;

import dev.cloud.model.User;

public interface UserService {
    public User signUp(User newUser);

    boolean signin(User entity);
}
