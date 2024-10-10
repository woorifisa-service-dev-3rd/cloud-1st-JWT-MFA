package dev.cloud.service;

import dev.cloud.model.User;
import dev.cloud.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;


    @Override
    public User signUp(User newUser) {
        return userRepository.save(newUser);
    }

    @Override
    public boolean signin(User entity) {
        Optional<User> findedUser=userRepository.findByEmail(entity.getEmail());
        if(findedUser.isPresent()){
            if(findedUser.get().getPw()==entity.getPw()){
                return true;
            }
        }
        return false;
    }
}
