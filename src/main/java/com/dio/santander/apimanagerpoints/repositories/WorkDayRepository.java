package com.dio.santander.apimanagerpoints.repositories;

import com.dio.santander.apimanagerpoints.models.WorkDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkDayRepository extends JpaRepository<WorkDay, Long> {
}
