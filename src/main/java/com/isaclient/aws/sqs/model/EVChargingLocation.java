package com.isaclient.aws.sqs.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import lombok.Data;
import org.hibernate.sql.ordering.antlr.GeneratedOrderByFragmentRendererTokenTypes;



import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "EVChargingLocation")
public class EVChargingLocation implements Serializable {
    @Id
    @Column(name = "_id")
    public String _id;
    @Column(name = "_status")
    public String _status;
    @Column(name = "access")
    public String access;
    @Column(name = "address")
    public String address;
    @Column(name = "city")
    public String city;
    @Column(name = "countryCodeIso")
    public String countryCodeIso;
    @Column(name = "countryCode")
    public String countryCode;
    @Column(name = "countryName")
    public String countryName;
    @Column(name = "description")
    public String description;
    @Column(name = "facilities")
    public String facilities;
   // @Column(columnDefinition = "geometry")
   //@Column(name = "geoPoint", columnDefinition = "POINT")
    @Column(name = "geoPoint" , columnDefinition = "POINT")
    public Point point;
    @Column(name = "name")
    public String name;
    @Column(name = "parkingType")
    public String parkingType;
    @Column(name = "postalCode")
    public String postalCode;
    @Column(name = "stateCode")
    public String stateCode;
    @Column(name = "stateName")
    public String stateName;
    @Column(name = "publish")
    public String publish;
    @Column(name = "lastUpdated")
    public Timestamp lastUpdated;




}
