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

    @Value("${data.admin_mail}")
    private String adminMail;

    @Value("${data.admin_pass}")
    private String adminPass;

    @Value("${data.admin_token_name}")
    private String adminTokenName;

    public RedirectView dashboardResponse(User user, RedirectAttributes redirectAttributes, HttpServletResponse response) {
        String eMail = user.getEmail(), password = user.getPassword();
        if(eMail.equals(adminMail) && password.equals(adminPass)) {
            Cookie adminTokenCookie = new Cookie(adminTokenName, Utils.generateBase64Token(eMail, password));
            adminTokenCookie.setMaxAge(3600);
            response.addCookie(adminTokenCookie);
            return new RedirectView("/admin");
        }
        redirectAttributes.addAttribute("base64t", Utils.generateBase64Token(eMail, password));
        return new RedirectView(hashDecryptUrl);
    }

}
