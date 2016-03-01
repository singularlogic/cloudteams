package eu.cloudteams.controller;

import java.util.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Controller
public class WebController {

    private static final Logger logger = Logger.getLogger(WebController.class.getName());

    /*
     *  GET Methods 
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        return "index";
    }


    @RequestMapping(value = "/api/v1/git/add", method = RequestMethod.GET)
    public String addGitToken(@RequestParam long uid) {
        logger.info("I was hit from Github with uid: " + uid);
        return "github";
    }

}
