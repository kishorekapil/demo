package com.isaclient.aws.sqs.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.geo.Point;

import java.util.List;


@Getter
@Setter
public class GeoPoint  {

    private String type;
    private List<String> coordinates;
}
