package eu.cloudteams.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public class Token {
	String token;

        public Token(){
            
        }
        
	public Token(@JsonProperty("token") String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}
}
