/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.cloudteams.util.paasport.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author jled
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SonarMetrics {
    
    private int technicalDebt;
    private String key ;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    private int linesOfCode;
    private String Rating;
    
    
    public int getTechnicalDebt() {
        return technicalDebt;
    }

    public void setTechnicalDebt(int technicalDebt) {
        this.technicalDebt = technicalDebt;
    }

    public int getLinesOfCode() {
        return linesOfCode;
    }

    public void setLinesOfCode(int linesOfCode) {
        this.linesOfCode = linesOfCode;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String Rating) {
        this.Rating = Rating;
    }

}
