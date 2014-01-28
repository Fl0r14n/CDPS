package com.threepillarglobal.labs.cdps.domain;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
"eventdate"
})
public class UserAverage {

@JsonProperty("eventdate")
private List<Eventdate> eventdate = new ArrayList<Eventdate>();

@JsonProperty("eventdate")
public List<Eventdate> getEventdate() {
return eventdate;
}

@JsonProperty("eventdate")
public void setEventdate(List<Eventdate> eventdate) {
this.eventdate = eventdate;
}


}