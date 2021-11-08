package com.isaclient.aws.sqs.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
public class EVChargingLocationVO implements Serializable {

    public String _id;

    public String _status;

    public String access;

    public String address;

    public String city;

    public String countryCodeIso;

    public String countryCode;

    public String countryName;

    public String description;

    public String facilities;

    public String name;

    public String parkingType;

    public String postalCode;

    public String stateCode;

    public String stateName;

    public String publish;

    public Timestamp lastUpdated;

    public GeoPoint geoPoint;






}
