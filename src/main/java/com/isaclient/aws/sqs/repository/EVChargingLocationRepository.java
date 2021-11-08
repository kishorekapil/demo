package com.isaclient.aws.sqs.repository;

import com.isaclient.aws.sqs.model.EVChargingLocation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EVChargingLocationRepository extends JpaRepository<EVChargingLocation, Long> {
    public EVChargingLocation findByName(String name);
}


