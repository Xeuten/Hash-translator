package tt.authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import tt.authorization.model.User;
import tt.authorization.persistence.UserRepository;
import tt.authorization.util.Utils;

import javax.servlet.http.HttpServletRequest;

@Service
public class AddUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Utils util;

    @Value("${data.admin_token_name}")
    private String adminTokenName;

    @Value("${messages.denied}")
    private String denied;

    @Value("${messages.user_exists}")
    private String userExists;

    @Value("${messages.user_saved}")
    private String userSaved;

    /**
     * This method adds a new entry to the database if the credentials passed to the method as parameters aren't in the
     * database and if the user has logged in as admin.
     * @param email The email that has been entered into form by the user.
     * @param password The password that has been entered into form by the user.
     * @param request The request that has been sent to the corresponding endpoint.
     * @param model The model that will be passed to the View.
     * @return The string that corresponds to the general Thymeleaf html template.
     */
    public String addUserResponse(String email, String password, HttpServletRequest request, Model model) {
        if(util.containsCookie(request, adminTokenName)) {
            if(userRepository.findById(email).isPresent()) {
                model.addAttribute("message", String.format(userExists, email));
            } else {
                userRepository.save(new User(email, password));
                model.addAttribute("message", String.format(userSaved, email));
            }
        } else model.addAttribute("message", denied);
        return "template1";
    }

}
