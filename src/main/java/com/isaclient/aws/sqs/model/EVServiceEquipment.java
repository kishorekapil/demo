package com.isaclient.aws.sqs.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
@Data
@Entity
@Table(name = "EVServiceEquipment" , schema = "isa-client")
public class EVServiceEquipment implements Serializable {
    @Column(name = "EVChargingLocation__id")
    public String EVChargingLocation__id;
    @Id
    @Column(name = "_id")
    public String _id;
    @Column(name = "_status")
    public String _status;
    @Column(name = "name")
    public String name;
    @Column(name = "network")
    public String network;
    @Column(name = "networkUrl")
    public String networkUrl;
    @Column(name = "status")
    public String status;
    @Column(name = "lastUpdated")
    public Timestamp lastUpdated;
}
