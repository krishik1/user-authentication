package user.authentication.flow.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import user.authentication.flow.model.Role;

import java.util.List;

@Data
@Builder
public class UserDto {
    private Long id;
    private String email;
    private List<Role> roles;
}
