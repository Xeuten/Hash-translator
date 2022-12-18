package tt.authorization.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import tt.authorization.model.User;
import tt.authorization.persistence.UserRepository;
import tt.authorization.util.Utils;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class AddUserService {

    @Autowired
    private UserRepository userRepository;

    public String addUserResponse(String email, String password, HttpServletRequest request, Model model) {
        if(Utils.containsCookie(request, "base64AdminToken")) {
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
