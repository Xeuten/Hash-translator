package tt.hashtranslator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import tt.hashtranslator.util.Utils;

import javax.servlet.http.HttpServletRequest;

@Service
public class ReceptionService {

    @Autowired
    private Utils util;

    @Value("${messages.login_error}")
    private String loginError;

    /**
     * This method returns a html form that allows user to enter an id to receive the result of the application
     * with the corresponding id.
     * @param request The request that has been sent to the corresponding endpoint.
     * @param model The model that will be passed to the View.
     * @return The string that corresponds to the html document, which corresponds to the submission form of an id of
     * an application.
     */
    public String receptionResponse(HttpServletRequest request, Model model) {
        if(util.getToken(request).isPresent()) {
            return "receive_result";
        }
        model.addAttribute("message", loginError);
        return "template1";
    }

}
