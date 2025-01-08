package user.authentication.flow.service;

import org.antlr.v4.runtime.misc.Pair;
import org.springframework.util.MultiValueMap;
import user.authentication.flow.model.User;

import java.util.Map;

public interface AuthServiceI {
    User signup(String email, String password);

    Pair<User, MultiValueMap<String,String>> login(String email, String password);

    Boolean validateToken(Long userId,String token);
}
