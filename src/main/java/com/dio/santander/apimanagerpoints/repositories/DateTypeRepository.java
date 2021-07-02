package com.dio.santander.apimanagerpoints.repositories;

import com.dio.santander.apimanagerpoints.models.DateType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DateTypeRepository extends JpaRepository<DateType, Long> {
}
