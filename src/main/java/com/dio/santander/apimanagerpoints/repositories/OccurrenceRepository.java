package com.dio.santander.apimanagerpoints.repositories;

import com.dio.santander.apimanagerpoints.models.Occurrence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OccurrenceRepository extends JpaRepository<Occurrence, Long> {
}
