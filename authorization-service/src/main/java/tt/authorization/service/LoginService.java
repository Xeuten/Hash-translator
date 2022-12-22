package tt.authorization.service;

import org.springframework.stereotype.Service;

@Service
public class LoginService {

    /**
     * This method returns a form that allows user to log in.
     * @return The string that corresponds to the login html document.
     */
    public String loginResponse() { return "login"; }

}
