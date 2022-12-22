package tt.authorization.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import tt.authorization.util.Utils;

import javax.servlet.http.HttpServletRequest;

@Service
public class AdminService {

    @Value("${data.admin_token_name}")
    private String adminTokenName;

    @Value("${messages.logged_as_admin}")
    private String loggedAsAdmin;

    @Value("${messages.denied}")
    private String denied;

    /**
     * This method returns a message that the user has logged in as admin. User is being redirected to the
     * corresponding endpoint after he enters the admin credentials to the form.
     * @param request The request that has been sent to the corresponding endpoint.
     * @param model The model that will be passed to the View.
     * @return The string that corresponds to the general Thymeleaf html template.
     */
    public String adminResponse(HttpServletRequest request, Model model) {
        model.addAttribute("message", Utils.containsCookie(request, adminTokenName) ? loggedAsAdmin : denied);
        return "template1";
    }

}
