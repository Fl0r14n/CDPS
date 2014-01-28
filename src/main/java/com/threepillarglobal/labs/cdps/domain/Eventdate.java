package com.threepillarglobal.labs.cdps.domain;

import java.util.Date;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
"eventdate",
"systolicPressure",
"diastolicPressure"
})
public class Eventdate {

@JsonProperty("eventdate")
private Date eventdate;
@JsonProperty("systolicPressure")
private Integer systolicPressure;
@JsonProperty("diastolicPressure")
private Integer diastolicPressure;


@JsonProperty("eventdate")
public Date getEventdate() {
return eventdate;
}

@JsonProperty("eventdate")
public void setEventdate(Date eventdate) {
this.eventdate = eventdate;
}

@JsonProperty("systolicPressure")
public Integer getSystolicPressure() {
return systolicPressure;
}

@JsonProperty("systolicPressure")
public void setSystolicPressure(Integer systolicPressure) {
this.systolicPressure = systolicPressure;
}

@JsonProperty("diastolicPressure")
public Integer getDiastolicPressure() {
return diastolicPressure;
}

@JsonProperty("diastolicPressure")
public void setDiastolicPressure(Integer diastolicPressure) {
this.diastolicPressure = diastolicPressure;
}



}