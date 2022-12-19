package tt.hashtranslator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tt.hashtranslator.util.Utils;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

@Service
public class TransitionalService {

    @Value("${urls.applications}")
    private String applicationsUrl;

    @Value("${messages.application_submitted}")
    private String applicationSubmitted;

    public String transitionalResponse(String decryptionRequest, Model model) {
        String hashesJSON = decryptionRequest.substring(0, decryptionRequest.lastIndexOf("}") + 1).split("=")[1];
        String temp = decryptionRequest.substring(decryptionRequest.lastIndexOf("}")).split("=", 2)[1];
        String token = temp.substring(0, temp.length() - 2);
        if(token.equals("")) return "login_error";
        try {
            HttpResponse<String> applicationResponse = HttpClient.newHttpClient().send(Utils
                    .buildPostRequest(applicationsUrl, hashesJSON, token), HttpResponse.BodyHandlers.ofString());
            if(applicationResponse.statusCode() == 202) {
                model.addAttribute("message", applicationSubmitted + applicationResponse.body());
                return "hashes_submitted";
            }
            model.addAttribute("message", applicationResponse.body());
            return "submission_error";
        } catch (IOException | InterruptedException e) {
            return "request_send_error";
        }
    }

}
