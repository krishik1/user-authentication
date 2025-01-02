package user.authentication.flow.controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import user.authentication.flow.dtos.LoginRequestDto;
import user.authentication.flow.dtos.SignUpRequestDto;
import user.authentication.flow.dtos.UserDto;
import user.authentication.flow.exception.UserAlreadyExistException;
import user.authentication.flow.model.User;
import user.authentication.flow.service.AuthServiceI;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthServiceI authService;
    public AuthController(AuthServiceI authService) {
        this.authService=authService;
    }


    @PostMapping("/signup")
    public UserDto signup(@RequestBody SignUpRequestDto signupRequestDto) {
        try {
            User user = authService.signup(signupRequestDto.getEmail(), signupRequestDto.getPassword());
            return UserDto.builder().email(user.getEmail()).id(user.getId()).roles(user.getRoles()).build();
        }catch (UserAlreadyExistException exception) {
            throw exception;
        }
    }

    @PostMapping("/login")
    public UserDto login(@RequestBody LoginRequestDto loginRequestDto) {
        User user = authService.login(loginRequestDto.getEmail(),loginRequestDto.getPassword());
        return UserDto.builder().email(user.getEmail()).id(user.getId()).roles(user.getRoles()).build();

    }
}
