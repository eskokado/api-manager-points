package com.dio.santander.apimanagerpoints.repositories;

import com.dio.santander.apimanagerpoints.models.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {
}
