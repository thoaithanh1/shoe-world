package root.app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import root.app.entity.Role;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDto {

    private Long userId;

    private String avatar;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private Boolean status;

    private Set<Role> roles;
}
