package tt.authorization.service;

import org.springframework.stereotype.Service;
import tt.authorization.util.Utils;

import javax.servlet.http.HttpServletRequest;


@Service
public class AdminService {

    public String adminResponse(HttpServletRequest request) {
        if(Utils.containsCookie(request, "base64AdminToken")) return "admin";
        return "denied";
    }

}
