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

//    @RequestMapping(value = "/login", method = RequestMethod.GET)
//    public String login(Model model) {
//        model.addAttribute("user", "addUserObject");
//        if (isUserLoggedIn()) {
//            return "redirect:/" + dashboard(model);
//        }
//        return "login";
//    }

//    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
//    public String dashboard(Model model) {
//        logger.log(Level.INFO, "Success login for user: {0} , with userID: {1} and role: {2}", new Object[]{getCurrentUser().getUsername(), getCurrentUser().getId(), getCurrentUser().getRole()});
////        model.addAttribute("currentUser", getCurrentUser());
//        return "dashboard";
//    }

    //views/remote-data
    @RequestMapping(value = "/views/remote-data", method = RequestMethod.GET)
    public String remoteData(Model model) {
//        model.addAttribute("currentUser", getCurrentUser());
        return "remote-data";
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
//    @RequestMapping(value = "/register", method = RequestMethod.POST)
//    public String registerSubmit(@ModelAttribute CurrentUser user, Model model) {
//        model.addAttribute("user", user);
//        ApplicationResponse appResponse = null;
//        String redirectToPage = "";
//
//        if (appResponse.getCode() == BasicResponseCode.SUCCESS) {
//            model.addAttribute("new_registration", appResponse.getMessage());
//            return login(model);
//        } else {
//            redirectToPage = "registration";
//            model.addAttribute("error", appResponse.getMessage());
//        }
//        logger.log(Level.INFO, "StatusCode: {0} StatusMessage: {1}", new Object[]{appResponse.getCode(), appResponse.getMessage()});
//        return redirectToPage;
//
//    }

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
