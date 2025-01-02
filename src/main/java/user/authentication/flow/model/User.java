package user.authentication.flow.model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;


@Setter
@Getter
@SuperBuilder
@Entity
@NoArgsConstructor
public class User extends BaseModel {
    private String email;
    private String password;
    @ManyToMany
    private List<Role> roles= new ArrayList<>();
}
