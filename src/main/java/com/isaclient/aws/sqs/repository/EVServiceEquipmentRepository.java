package com.isaclient.aws.sqs.repository;

import com.isaclient.aws.sqs.model.EVServiceEquipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EVServiceEquipmentRepository extends JpaRepository<EVServiceEquipment, String> {
        }
