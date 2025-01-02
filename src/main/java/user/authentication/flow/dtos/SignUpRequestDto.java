package user.authentication.flow.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
public class SignUpRequestDto {
    private String email;
    private String password;


}
