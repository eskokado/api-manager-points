package com.dio.santander.apimanagerpoints.repositories;

import com.dio.santander.apimanagerpoints.models.BankOfHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BankOfHourRepository extends JpaRepository<BankOfHour, Long> {
    @Query("select boh from BankOfHour boh where boh.id.bankOfHourId = ?1 and boh.id.movementId =?2 and boh.id.userId = ?3")
    BankOfHour findByPK(Long bankOfHourId, Long movementId, Long userId);
}
