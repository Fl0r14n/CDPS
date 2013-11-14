package com.threepillarglobal.labs.cdps.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class Location {

    private final Long id;
    private final String address;
    private final Double latitude;
    private final Double longitude;
}
