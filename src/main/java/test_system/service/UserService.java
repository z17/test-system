package test_system.service;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import test_system.data.UserData;
import test_system.entity.Role;
import test_system.entity.UserEntity;
import test_system.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bcryptEncoder;

    @Autowired
    public UserService(final UserRepository userRepository, final BCryptPasswordEncoder bcryptEncoder) {
        this.userRepository = userRepository;
        this.bcryptEncoder = bcryptEncoder;
    }

    public UserEntity save(final UserData user) {
        val userEntity = new UserEntity();
        userEntity.setName(user.getName());
        userEntity.setLogin(user.getLogin());
        userEntity.setPassword(bcryptEncoder.encode(user.getPassword()));
        userEntity.setRole(Role.ROLE_USER);
        return userRepository.save(userEntity);
    }
}