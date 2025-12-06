package com.pontificia.remashorario.modules.user;

import com.pontificia.remashorario.utils.abstractBase.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends BaseRepository<UserEntity> {
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
    List findByRole(UserEntity.UserRole role);
    List findByRoleAndActive(UserEntity.UserRole role, Boolean active);
}
