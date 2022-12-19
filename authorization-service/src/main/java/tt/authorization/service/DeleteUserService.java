package tt.authorization.service;

import org.springframework.beans.factory.annotation.Autowired;
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

    public String deleteUserResponse(String email, HttpServletRequest request, Model model) {
        if(Utils.containsCookie(request, "base64AdminToken")) {
            Optional<User> user = userRepository.findById(email);
            if(user.isEmpty()) {
                model.addAttribute("message","Error. User " + email + " doesn't exist.");
                return "doesn't_exist";
            }
            userRepository.delete(user.get());
            model.addAttribute("message", "User " + email + " deleted successfully.");
            return "user_deleted";
        }
        return "denied";
    }

}
