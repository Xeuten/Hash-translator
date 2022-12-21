package tt.hashtranslator.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class SubmitService {

    public String submitResponse(String token, Model model) {
        if(token.equals("")) return "login_error";
        model.addAttribute("base64t", token);
        return "submit_hashes";
    }

}
