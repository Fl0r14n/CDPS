package com.threepillarglobal.labs.hbase.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Describes an HBase Column Family.<br/>
 * An HColumnFamily can be used on fields or at class level if let's say you
 * have a table with only one colymn family which acts as the default column
 * family
 */
@Documented
@Target(value = {ElementType.TYPE, ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface HColumnFamily {

    /**
     * HBase column family name
     *
     * @return column family name
     */
    public String name();
}
