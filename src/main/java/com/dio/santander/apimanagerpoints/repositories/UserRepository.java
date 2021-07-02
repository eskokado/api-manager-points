package com.dio.santander.apimanagerpoints.repositories;

import com.dio.santander.apimanagerpoints.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
