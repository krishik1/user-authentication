package user.authentication.flow.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ValidateTokenRequest {
    private Long userId;
    private String token;
}
