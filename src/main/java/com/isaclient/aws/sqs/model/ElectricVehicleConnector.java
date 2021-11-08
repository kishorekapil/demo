package com.isaclient.aws.sqs.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Data
@Entity
@Table(name = "EVConnector")
public class ElectricVehicleConnector implements Serializable {
    @Column(name = "EVServiceEquipment__id")
    public String EVServiceEquipment__id;
    @Id
    @Column(name = "_id")
    public String _id;
    @Column(name = "_status")
    public String _status;
    @Column(name = "connector")
    public String connector;
    @Column(name = "format")
    public String format;
    @Column(name = "maxAmperage")
    public Integer maxAmperage;
    @Column(name = "maxElectricPower")
    public Integer maxElectricPower;
    @Column(name = "maxVoltage")
    public Integer maxVoltage;
    @Column(name = "powerType")
    public String powerType;
    @Column(name = "lastUpdated")
    public Timestamp lastUpdated;




}
