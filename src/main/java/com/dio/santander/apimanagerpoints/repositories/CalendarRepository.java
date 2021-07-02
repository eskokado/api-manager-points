package com.dio.santander.apimanagerpoints.repositories;

import com.dio.santander.apimanagerpoints.models.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long> {
}
