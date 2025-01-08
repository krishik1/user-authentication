package user.authentication.flow.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import user.authentication.flow.exception.IncorrectPasswordException;
import user.authentication.flow.exception.UserAlreadyExistException;
import user.authentication.flow.exception.UserDoesNotExist;
import user.authentication.flow.model.Status;
import user.authentication.flow.model.User;
import user.authentication.flow.model.UserSession;
import user.authentication.flow.repo.SessionRepo;
import user.authentication.flow.repo.UserRepo;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService implements  AuthServiceI {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private SessionRepo sessionRepo;

    @Autowired
    private SecretKey secretKey;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;



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
    public Pair<User, MultiValueMap<String,String>> login(String email, String password) {
        Optional<User> user = userRepo.findByEmail(email);
        if(user.isEmpty()) {
            throw new UserDoesNotExist("Please signup first");
        }
        if(!bCryptPasswordEncoder.matches(password,user.get().getPassword())) {
            throw new IncorrectPasswordException("Your Password didn't match. Please Enter Correct Password.");
        }


            Long currentTimeInMillis = System.currentTimeMillis();
            Map<String,Object> userClaims = new HashMap<>();
            userClaims.put("userId",user.get().getId());
            userClaims.put("permissions",user.get().getRoles());

            userClaims.put("iat",currentTimeInMillis);
            userClaims.put("expiry",currentTimeInMillis+86400000);
            userClaims.put("issuer","scalar");
            String token = Jwts.builder().claims(userClaims).signWith(secretKey).compact();


        MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
        headers.add(HttpHeaders.SET_COOKIE,token);

        UserSession userSession = UserSession.builder().token(token).user(user.get()).status(Status.ACTIVE).build();
        sessionRepo.save(userSession);
        Pair<User, MultiValueMap<String,String>> response = new Pair<>(user.get(),headers);

        return response;
    }

    public Boolean validateToken(Long userId,String token) {
        Optional<UserSession> userSession = sessionRepo.findByTokenAndUser_Id(token,userId);
        if(userSession.isEmpty()) return false;
        String persistedToken = userSession.get().getToken();
        JwtParser jwtParser = Jwts.parser().verifyWith(secretKey).build();
        Claims userClaims = jwtParser.parseSignedClaims(persistedToken).getPayload();
        Long expiryTime = (Long) userClaims.get("expiry");
        Long currentTime = System.currentTimeMillis();
        System.out.println(currentTime);
        System.out.println(expiryTime);
        if(currentTime>expiryTime) {
            sessionRepo.save(userSession.get().builder().status(Status.INACTIVE).build());
            return false;
        }

        return true;
    }
}
