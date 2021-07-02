package com.dio.santander.apimanagerpoints.repositories;

import com.dio.santander.apimanagerpoints.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}
