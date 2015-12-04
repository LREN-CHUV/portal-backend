/**
 * Created by mirco on 04.12.15.
 */

package org.hbp.mip.mock;

import org.hbp.mip.model.User;

public class UserMock extends User {
    public UserMock(int id) {
        this.setId(id);
        switch (id) {
            case 1:
                this.setFirstname("Linda");
                this.setFullname("Linda DIB");
                this.setLastname("DIB");
                this.setPicture("images/users/Linda.jpg");
                this.setWeb("http://www.hbpproject.eu");
                this.setPhone("+33 6 00 00 00 00");
                this.setBirthday("1982-01-22");
                this.setGender("Female");
                this.addLanguage("French");
                this.addLanguage("English");
                this.setPassword("user");
                this.setEmail("l.dib@hbpproject.eu");
                this.setIsActive(true);
                this.setApikey("kiliaanapikey");
                this.setTeam("Scientist");
                this.addRole("ROLE_ADMIN");
                this.addRole("ROLE_SCIENTIST");
                break;
        }
    }
}
