package user.authentication.flow.controller;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import user.authentication.flow.dtos.LoginRequestDto;
import user.authentication.flow.dtos.SignUpRequestDto;
import user.authentication.flow.dtos.UserDto;
import user.authentication.flow.dtos.ValidateTokenRequest;
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
    public ResponseEntity<UserDto> signup(@RequestBody SignUpRequestDto signupRequestDto) {
        try {
            User user = authService.signup(signupRequestDto.getEmail(), signupRequestDto.getPassword());
            UserDto userDto = UserDto.builder().email(user.getEmail()).id(user.getId()).roles(user.getRoles()).build();
            return new ResponseEntity<>(userDto,HttpStatusCode.valueOf(200));
        }catch (UserAlreadyExistException exception) {
            throw exception;
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        Pair<User, MultiValueMap<String,String>> response = authService.login(loginRequestDto.getEmail(),loginRequestDto.getPassword());
        User user = response.a;
        UserDto userDto = UserDto.builder().email(user.getEmail()).id(user.getId()).roles(user.getRoles()).build();
        return new ResponseEntity<>(userDto,response.b,HttpStatusCode.valueOf(200));

    }

    @PostMapping("/validateToken")
    public boolean validateToken(@RequestBody ValidateTokenRequest validateTokenRequest) {
        return authService.validateToken(validateTokenRequest.getUserId(),validateTokenRequest.getToken());
    }
}
