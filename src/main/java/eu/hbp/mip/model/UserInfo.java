package eu.hbp.mip.model;

import eu.hbp.mip.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class UserInfo {

    @Autowired
    private UserRepository userRepository;

    /**
     * Enable HBP collab authentication (1) or disable it (0). Default is 1
     */
    @Value("#{'${hbp.authentication.enabled:1}'}")
    private boolean authentication;

    private User user;

    /**
     * returns the user for the current session.
     * <p>
     * the "synchronized" keyword is there to avoid a bug that the transaction is supposed to protect me from.
     * To test if your solution to removing it works, do the following:
     * - clean DB from scratch
     * - restart DB and backend (no session or anything like that)
     * - log in using the front end
     * - check you have no 500 error in the network logs.
     *
     * @return the user for the current session
     */
    public User getUser() {
        if (user == null) {

            if (!authentication) {
                user = new User();
                user.setUsername("anonymous");
                user.setFullname("anonymous");
                user.setPicture("images/users/default_user.png");
            } else {
                user = new User(getUserInfos());
            }
            User foundUser = userRepository.findOne(user.getUsername());
            if (foundUser != null) {
                user.setAgreeNDA(foundUser.getAgreeNDA());
            }
            userRepository.save(user);
        }

        return user;
    }

    private String getUserInfos() {
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();
        return userAuthentication.getDetails().toString();
    }

}
