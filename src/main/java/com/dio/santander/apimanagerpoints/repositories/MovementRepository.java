package com.dio.santander.apimanagerpoints.repositories;

import com.dio.santander.apimanagerpoints.models.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {
    @Query("select m from Movement m where m.id.movementId =?1 and m.id.userId = ?2")
    Movement findByPK(Long movementId, Long userId);
}
