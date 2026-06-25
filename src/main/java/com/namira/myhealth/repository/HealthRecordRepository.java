//An interface is different from a class — it declares what methods should exist without writing their bodies.

        package com.namira.myhealth.repository;

import com.namira.myhealth.model.HealthRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthRecordRepository extends JpaRepository<HealthRecord, Long> {
}

//JpaRepository--This is where it happens. By extending Spring's JpaRepository, your interface inherits a whole set of ready-made methods that Spring writes for you behind the scenes: