package eu.cloudteams.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@RestController
@RequestMapping("/api/v1")
public class SonarQubeRestController {

    @RequestMapping(value = "/sonar", method = RequestMethod.GET)
    public String egtSonar() {
        return "Sonar Controller!";
    }

}
