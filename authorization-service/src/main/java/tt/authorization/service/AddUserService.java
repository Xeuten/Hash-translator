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

    @Value("${data.admin_token_name}")
    private String adminTokenName;

    public String addUserResponse(String email, String password, HttpServletRequest request, Model model) {
        if(Utils.containsCookie(request, adminTokenName)) {
            if(userRepository.findById(email).isPresent()) {
                model.addAttribute("message",   "Error. User " + email + " already exists.");
                return "already_exists";
            }
            userRepository.save(new User(email, password));
            model.addAttribute("message", "User " + email + " saved successfully.");
            return "user_saved";
        }
        return "denied";
    }

}
