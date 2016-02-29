package eu.cloudteams.controller;

//import eu.cloudteams.authentication.CurrentUser;
import java.util.logging.Logger;
//import org.springframework.security.core.context.SecurityContextHolder;
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
//        if (isUserLoggedIn()) {
//            return "redirect:/" + dashboard(model);
//        }
        return "index";
    }


    @RequestMapping(value = "/api/v1/git/add", method = RequestMethod.GET)
//    @ResponseBody
    public String addGitToken(@RequestParam long uid) {
        logger.info("I was hit from Github with uid: " + uid);
        return "github";
    }

    /*
     *  POST Methods 
     */

    /*
     *  Help Methods
     */
    /**
     * Retrieve the current logged-in user
     *
     * @return An instance of CurrentUser object
     */
//    public static CurrentUser getCurrentUser() {
//        return (CurrentUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//    }

    /**
     * Get current logged-in role
     *
     * @return Role name
     */
//    public static String getCurrentRole() {
//        return getCurrentUser().getRole();
//    }

    /**
     * Check if a user is logged-in
     *
     * @return True if user is logged-in, otherwise returns false
     */
//    private boolean isUserLoggedIn() {
//        try {
//            getCurrentUser();
//        } catch (ClassCastException ex) {
//            return false;
//        }
//        return true;
//    }
}
