package test_system.service;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import test_system.entity.UserEntity;
import test_system.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String login) throws UsernameNotFoundException {
        val user = userRepository.findByLogin(login);
        List<GrantedAuthority> authorities =
                buildUserAuthority(user.getRole().toString());

        return buildUserForAuthentication(user, authorities);
    }

    private User buildUserForAuthentication(UserEntity userEntity,
                                            List<GrantedAuthority> authorities) {
        return new User(userEntity.getLogin(), userEntity.getPassword(),
                true, true, true, true, authorities);
    }

    private List<GrantedAuthority> buildUserAuthority(String group) {
        Set<GrantedAuthority> setAuths = new HashSet<>();
        setAuths.add(new SimpleGrantedAuthority(group));
        return new ArrayList<>(setAuths);
    }
}
