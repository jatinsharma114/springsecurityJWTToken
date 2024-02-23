package com.springsecurityInmemory.pringsecurity.Dao;

import com.springsecurityInmemory.pringsecurity.Entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
public interface UserRepository extends JpaRepository<UserInfo, Integer> {
    Optional<UserInfo> findByUserName(String name);
}
