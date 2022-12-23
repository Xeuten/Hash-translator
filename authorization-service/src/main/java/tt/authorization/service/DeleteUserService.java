package tt.authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import tt.authorization.model.User;
import tt.authorization.persistence.UserRepository;
import tt.authorization.util.Utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class DeleteUserService {

    @Autowired
    private UserRepository userRepository;

    @Value("${data.admin_token_name}")
    private String adminTokenName;

    @Value("${messages.denied}")
    private String denied;

    /**
     * This method deletes an entry to the database if the credentials passed to the method as parameters are in the
     * database and if the user has logged in as admin.
     *
     * @param email The email that has been entered into form by the user.
     * @param request The request that has been sent to the corresponding endpoint.
     * @param model The model that will be passed to the View.
     * @return The string that corresponds to the general Thymeleaf html template.
     */
    public String deleteUserResponse(String email, HttpServletRequest request, Model model) {
        if(Utils.containsCookie(request, adminTokenName)) {
            Optional<User> user = userRepository.findById(email);
            if(user.isEmpty()) {
                model.addAttribute("message","Error. User " + email + " doesn't exist.");
            } else {
                userRepository.delete(user.get());
                model.addAttribute("message", "User " + email + " deleted successfully.");
            }
        } else model.addAttribute("message", denied);
        return "template1";
    }

}
