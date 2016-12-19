package test_system.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import test_system.entity.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserData {
    private String name;
    private String login;
    private String password;
    private Role role;

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}
