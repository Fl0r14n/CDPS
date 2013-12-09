package com.threepillarglobal.labs.cdps.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {

    private final String id;
    private final String name;
    private final String dob;
}
