/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.cloudteams.util.sonarqube;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"val",
"frmt_val",
"key"
})
public class Msr {

@JsonProperty("val")
private Integer val;
@JsonProperty("frmt_val")
private String frmtVal;
@JsonProperty("key")
private String key;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The val
*/
@JsonProperty("val")
public Integer getVal() {
return val;
}

/**
* 
* @param val
* The val
*/
@JsonProperty("val")
public void setVal(Integer val) {
this.val = val;
}

/**
* 
* @return
* The frmtVal
*/
@JsonProperty("frmt_val")
public String getFrmtVal() {
return frmtVal;
}

/**
* 
* @param frmtVal
* The frmt_val
*/
@JsonProperty("frmt_val")
public void setFrmtVal(String frmtVal) {
this.frmtVal = frmtVal;
}

/**
* 
* @return
* The key
*/
@JsonProperty("key")
public String getKey() {
return key;
}

/**
* 
* @param key
* The key
*/
@JsonProperty("key")
public void setKey(String key) {
this.key = key;
}

@JsonAnyGetter
public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

@JsonAnySetter
public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

}   


