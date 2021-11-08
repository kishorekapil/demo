package com.isaclient.aws.sqs.repository;

import com.isaclient.aws.sqs.model.ElectricVehicleConnector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvConnectorRepository extends JpaRepository<ElectricVehicleConnector , String> {
}
