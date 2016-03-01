/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.cloudteams.util.sonarqube;

/**
 *
 * @author jled
 */
public class SonarIssues {
    
    private int numberOfProjectIssues;
    private int numberofUserIssues;

    public int getNumberOfProjectIssues() {
        return numberOfProjectIssues;
    }

    public void setNumberOfProjectIssues(int numberOfProjectIssues) {
        this.numberOfProjectIssues = numberOfProjectIssues;
    }

    public int getNumberofUserIssues() {
        return numberofUserIssues;
    }

    public void setNumberofUserIssues(int numberofUserIssues) {
        this.numberofUserIssues = numberofUserIssues;
    }

    
}
