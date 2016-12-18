package test_system.repository;

import org.springframework.data.repository.CrudRepository;
import test_system.entity.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByLogin(final String login);
}
