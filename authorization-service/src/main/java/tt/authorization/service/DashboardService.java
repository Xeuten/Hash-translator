package tt.authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import tt.authorization.model.User;
import tt.authorization.util.Utils;

import javax.servlet.http.HttpServletResponse;

@Service
public class DashboardService {

    @Autowired
    private Utils util;

    @Value("${urls.applications}")
    private String hashDecryptUrl;

    @Value("${data.admin_mail}")
    private String adminMail;

    @Value("${data.admin_pass}")
    private String adminPass;

    @Value("${data.token_name}")
    private String tokenName;

    /**
     * This method corresponds to a transitional controller. If the user has entered admin credentials into form, a
     * cookie that is used to identify admin during further requests is created. After that the user is redirected
     * to the admin URL. If the user has entered any other credentials, the token is created and is added to redirect
     * attributes, and the user is redirected to the translator service.
     * @param user The object that contains credentials that were entered.
     * @param redirectAttributes The object that contains authorization token.
     * @param response The object that saves admin identification cookie.
     * @return The object that redirects the user to the specified url.
     */
    public RedirectView dashboardResponse(User user, RedirectAttributes redirectAttributes, HttpServletResponse response) {
        String email = user.getEmail(), password = user.getPassword();
        if(email.equals(adminMail) && password.equals(adminPass)) {
            util.createAdminCookie(response, user);
            return new RedirectView("/admin");
        }
        redirectAttributes.addAttribute(tokenName, util.generateBase64Token(email, password));
        return new RedirectView(hashDecryptUrl);
    }

}
