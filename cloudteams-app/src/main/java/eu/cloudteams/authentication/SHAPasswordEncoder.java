//package eu.cloudteams.authentication;
//
//import eu.cloudteams.util.other.Util;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
///**
// *
// * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
// */
//public class SHAPasswordEncoder implements PasswordEncoder {
//
//    @Override
//    public String encode(CharSequence cs) {
//        return cs.toString();
//    }
//
//    @Override
//    public boolean matches(CharSequence cs, String string) {
//        return Util.createAlgorithm(cs.toString(), "SHA").equals(string);
//    }
//
//}
