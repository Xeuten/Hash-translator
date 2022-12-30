package tt.hashtranslator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import tt.hashtranslator.util.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
public class SubmissionService {

    @Autowired
    private Utils util;

    @Value("${messages.login_error}")
    private String loginError;

    /**
     * This method corresponds to the initial endpoint, where the user is redirected from the authorization service.
     * This method checks if the user token is present either as a model attribute that was added in the authorization
     * service or as an already existing cookie. If the token is present, returns a html form that allows user to
     * submit a JSON of hashes to decrypt.
     * @param token The Base64 token, which was passed from the authorization service.
     * @param response HttpServletResponse object that will store user's token.
     * @param request The request that has been sent to the corresponding endpoint.
     * @param model The model that will be passed to the View.
     * @return The string that corresponds to the html document of the submission form of a JSON of the hashes to
     * decrypt.
     */
    public String submissionResponse(String token, HttpServletResponse response, HttpServletRequest request,
                                     Model model) {
        Optional<String> existingToken = util.getToken(request);
        if(token.equals("") && existingToken.isEmpty()) {
            model.addAttribute("message", loginError);
            return "template1";
        }
        util.createUserCookie(response, token.equals("") ? existingToken.get() : token);
        return "submit_hashes";
    }

}
