package tt.hashtranslator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class SubmitService {

    @Value("${data.token_name}")
    private String tokenName;

    @Value("${messages.login_error}")
    private String loginError;

    /**
     * This method returns a html form that allows user to submit a JSON of hashes to decrypt. This method corresponds
     * to the initial endpoint, where the user is redirected from the authorization service.
     * @param token The Base64 token, which was passed from the authorization service.
     * @param response The request that has been sent to the corresponding endpoint.
     * @param model The model that will be passed to the View.
     * @return The string that corresponds to the html document, which corresponds to the submission form of a JSON of
     * the hashes to decrypt.
     */
    public String submitResponse(String token, HttpServletResponse response, Model model) {
        if(token.equals("")) {
            model.addAttribute("message", loginError);
            return "template1";
        }
        Cookie tokenCookie = new Cookie(tokenName, token);
        tokenCookie.setMaxAge(1800);
        response.addCookie(tokenCookie);
        return "submit_hashes";
    }

}
