package eu.cloudteams.util.sonarqube.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"date",
"lname",
"scope",
"qualifier",
"name",
"description",
"id",
"uuid",
"version",
"msr",
"key"
})
/**
 *
 * @author jled
 */
public class SonarMetricsResponse {
 



@JsonProperty("date")
private String date;
@JsonProperty("lname")
private String lname;
@JsonProperty("scope")
private String scope;
@JsonProperty("qualifier")
private String qualifier;
@JsonProperty("name")
private String name;
@JsonProperty("description")
private String description;
@JsonProperty("id")
private Integer id;
@JsonProperty("uuid")
private String uuid;
@JsonProperty("version")
private String version;
@JsonProperty("msr")
private List<Msr> msr = new ArrayList<Msr>();
@JsonProperty("key")
private String key;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

/**
* 
* @return
* The date
*/
@JsonProperty("date")
public String getDate() {
return date;
}

/**
* 
* @param date
* The date
*/
@JsonProperty("date")
public void setDate(String date) {
this.date = date;
}

/**
* 
* @return
* The lname
*/
@JsonProperty("lname")
public String getLname() {
return lname;
}

/**
* 
* @param lname
* The lname
*/
@JsonProperty("lname")
public void setLname(String lname) {
this.lname = lname;
}

/**
* 
* @return
* The scope
*/
@JsonProperty("scope")
public String getScope() {
return scope;
}

/**
* 
* @param scope
* The scope
*/
@JsonProperty("scope")
public void setScope(String scope) {
this.scope = scope;
}

/**
* 
* @return
* The qualifier
*/
@JsonProperty("qualifier")
public String getQualifier() {
return qualifier;
}

/**
* 
* @param qualifier
* The qualifier
*/
@JsonProperty("qualifier")
public void setQualifier(String qualifier) {
this.qualifier = qualifier;
}

/**
* 
* @return
* The name
*/
@JsonProperty("name")
public String getName() {
return name;
}

/**
* 
* @param name
* The name
*/
@JsonProperty("name")
public void setName(String name) {
this.name = name;
}

/**
* 
* @return
* The description
*/
@JsonProperty("description")
public String getDescription() {
return description;
}

/**
* 
* @param description
* The description
*/
@JsonProperty("description")
public void setDescription(String description) {
this.description = description;
}

/**
* 
* @return
* The id
*/
@JsonProperty("id")
public Integer getId() {
return id;
}

/**
* 
* @param id
* The id
*/
@JsonProperty("id")
public void setId(Integer id) {
this.id = id;
}

/**
* 
* @return
* The uuid
*/
@JsonProperty("uuid")
public String getUuid() {
return uuid;
}

/**
* 
* @param uuid
* The uuid
*/
@JsonProperty("uuid")
public void setUuid(String uuid) {
this.uuid = uuid;
}

/**
* 
* @return
* The version
*/
@JsonProperty("version")
public String getVersion() {
return version;
}

/**
* 
* @param version
* The version
*/
@JsonProperty("version")
public void setVersion(String version) {
this.version = version;
}

/**
* 
* @return
* The msr
*/
@JsonProperty("msr")
public List<Msr> getMsr() {
return msr;
}

/**
* 
* @param msr
* The msr
*/
@JsonProperty("msr")
public void setMsr(List<Msr> msr) {
this.msr = msr;
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


