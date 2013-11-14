package com.threepillarglobal.labs.cdps.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class User {

    private final String name;
    private final String email;
    private final String password;
}
