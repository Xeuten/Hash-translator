package tt.authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import tt.authorization.model.User;
import tt.authorization.persistence.UserRepository;
import tt.authorization.util.Utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class DashboardService {

    @Autowired
    private UserRepository userRepository;

    @Value("${urls.applications}")
    private String hashDecryptUrl;

    @Value("${messages.incorrect_credentials}")
    private String incorrectCredentials;

    @Value("${data.admin_mail}")
    private String adminMail;

    @Value("${data.admin_pass}")
    private String adminPass;

        public RedirectView dashboardResponse(User user, RedirectAttributes redirectAttributes, HttpServletResponse response) {
        String eMail = user.getEmail(), password = user.getPassword();
        if(eMail.equals(adminMail) && password.equals(adminPass)) {
            response.addCookie(new Cookie("base64AdminToken", Utils.generateBase64Token(eMail, password)));
            return new RedirectView("/admin");
        }
        if(userRepository.credentialsAreCorrect(eMail, password)) {
            redirectAttributes.addFlashAttribute("base64t", Utils.generateBase64Token(eMail, password));
            return new RedirectView(hashDecryptUrl);
        }
        redirectAttributes.addFlashAttribute("message", incorrectCredentials);
        return new RedirectView("/login");
    }
}
