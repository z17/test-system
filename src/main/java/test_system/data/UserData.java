package test_system.data;

import lombok.*;
import test_system.entity.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserData {
    private String name;
    private String login;
    private String password;
    private Role role;
}
