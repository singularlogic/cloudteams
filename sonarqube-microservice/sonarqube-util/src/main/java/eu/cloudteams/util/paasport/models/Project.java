package eu.cloudteams.util.paasport.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import javax.annotation.Generated;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "id",
    "k",
    "nm",
    "sc",
    "qu"
})
public class Project {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("k")
    private String k;
    @JsonProperty("nm")
    private String nm;
    @JsonProperty("sc")
    private String sc;
    @JsonProperty("qu")
    private String qu;

    /**
     *
     * @return The id
     */
    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id The id
     */
    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return The k
     */
    @JsonProperty("k")
    public String getK() {
        return k;
    }

    /**
     *
     * @param k The k
     */
    @JsonProperty("k")
    public void setK(String k) {
        this.k = k;
    }

    /**
     *
     * @return The nm
     */
    @JsonProperty("nm")
    public String getNm() {
        return nm;
    }

    /**
     *
     * @param nm The nm
     */
    @JsonProperty("nm")
    public void setNm(String nm) {
        this.nm = nm;
    }

    /**
     *
     * @return The sc
     */
    @JsonProperty("sc")
    public String getSc() {
        return sc;
    }

    /**
     *
     * @param sc The sc
     */
    @JsonProperty("sc")
    public void setSc(String sc) {
        this.sc = sc;
    }

    /**
     *
     * @return The qu
     */
    @JsonProperty("qu")
    public String getQu() {
        return qu;
    }

    /**
     *
     * @param qu The qu
     */
    @JsonProperty("qu")
    public void setQu(String qu) {
        this.qu = qu;
    }

}
