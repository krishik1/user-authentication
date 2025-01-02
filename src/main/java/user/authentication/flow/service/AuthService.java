package user.authentication.flow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import user.authentication.flow.exception.IncorrectPasswordException;
import user.authentication.flow.exception.UserAlreadyExistException;
import user.authentication.flow.exception.UserDoesNotExist;
import user.authentication.flow.model.User;
import user.authentication.flow.repo.UserRepo;

import java.util.Optional;

@Service
public class AuthService implements  AuthServiceI {
    @Autowired
    private UserRepo userRepo;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public User signup(String email, String password) {
        Optional<User> user = userRepo.findByEmail(email);
        if(user.isPresent()) {
            throw new UserAlreadyExistException("Please try with different email !!");
        }
        User newUser = userRepo.save(User.builder().email(email).password(bCryptPasswordEncoder.encode(password)).build());

        return newUser;
    }

    @Override
    public User login(String email, String password) {
        Optional<User> user = userRepo.findByEmail(email);
        if(user.isEmpty()) {
            throw new UserDoesNotExist("Please signup first");
        }
        if(!bCryptPasswordEncoder.matches(password,user.get().getPassword())) {
            throw new IncorrectPasswordException("Your Password didn't match. Please Enter Correct Password.");
        }
        return user.get();
    }
}
