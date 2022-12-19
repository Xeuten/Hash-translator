package tt.authorization.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tt.authorization.util.Utils;

import javax.servlet.http.HttpServletRequest;


@Service
public class AdminService {

    @Value("${data.admin_token_name}")
    private String adminTokenName;

    public String adminResponse(HttpServletRequest request) {
        if(Utils.containsCookie(request, adminTokenName)) return "admin";
        return "denied";
    }

}
