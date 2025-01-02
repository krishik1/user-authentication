package user.authentication.flow.service;

import user.authentication.flow.model.User;

public interface AuthServiceI {
    User signup(String email, String password);

    User login(String email, String password);
}
