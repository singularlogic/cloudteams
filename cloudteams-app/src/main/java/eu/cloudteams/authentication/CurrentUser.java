//package eu.cloudteams.authentication;
//
//import eu.cloudteams.repository.domain.UserRole;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.core.userdetails.User;
//
///**
// *
// * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
// */
//public class CurrentUser extends User {
//
//    private final long userID;
//    private final long roleID;
//    private final String firstName;
//    private final String lastName;
//
//    public CurrentUser(long userID, String username, String password, UserRole role, String firstName, String lastName) {
//        super(username, password, AuthorityUtils.createAuthorityList(role.getName()));
//        this.userID = userID;
//        this.roleID = role.getId();
//        this.firstName = firstName;
//        this.lastName = lastName;
//    }
//
//    public long getId() {
//        return userID;
//    }
//
//    public String getRole() {
//        return super.getAuthorities().toArray()[0].toString();
//    }
//
//    public long getRoleId() {
//        return roleID;
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//
//    public String getLastName() {
//        return lastName;
//    }
//
//}
