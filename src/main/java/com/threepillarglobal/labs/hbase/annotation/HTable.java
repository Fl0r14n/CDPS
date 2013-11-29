package com.threepillarglobal.labs.hbase.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Describes an HBase table
 */
@Documented
@Target(value = {ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface HTable {
    
    /**
     * HBase table name
     * @return table name
     */
    public String name();
    
    /**
     * HBase column tables associated to this table
     * @return column families
     */
    public String[] columnFamilies() default {};
}
