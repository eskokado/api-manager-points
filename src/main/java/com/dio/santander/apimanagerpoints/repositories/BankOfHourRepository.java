package com.dio.santander.apimanagerpoints.repositories;

import com.dio.santander.apimanagerpoints.models.BankOfHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankOfHourRepository extends JpaRepository<BankOfHour, Long> {
}
