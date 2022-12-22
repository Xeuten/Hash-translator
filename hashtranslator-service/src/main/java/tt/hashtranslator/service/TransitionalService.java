package tt.hashtranslator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import tt.hashtranslator.util.Utils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Optional;

@Service
public class TransitionalService {

    @Value("${urls.applications}")
    private String applicationsUrl;

    @Value("${urls.applications}")
    private String receiveResult;

    @Value("${messages.application_submitted}")
    private String applicationSubmitted;

    @Value("${messages.send_error}")
    private String sendError;

    @Value("${messages.login_error}")
    private String loginError;

    @Value("${messages.url_error}")
    private String urlError;

    @Value("${messages.request_error}")
    private String requestError;

    @Value("${data.token_name}")
    private String tokenName;

    /**
     * This method corresponds to the controller, which acts as a bridge from the controllers that view to user the
     * input forms to the controllers which process the user's input. It sends the requests to them and adds their
     * answers to the View.
     * @param isSubmit The boolean value adjusts the method, depending on which of the controllers has called it.
     * @param rawData The variable contain either a raw JSON of hashes to submit, or a raw id of an application.
     * @param request The request that has been sent to the corresponding endpoint.
     * @param model The model that will be passed to the View.
     * @return The string that corresponds to the general Thymeleaf html template
     */
    public String transitionalResponse(boolean isSubmit, String rawData, HttpServletRequest request, Model model) {
        try {
            String data = rawData.split("=")[1];
            Optional<String> token = Utils.getToken(request);
            if(token.isPresent()) {
                HttpResponse<String> applicationResponse = HttpClient.newHttpClient().send(isSubmit ?
                        Utils.buildPostRequest(applicationsUrl, data, token.get()) :
                        Utils.buildGetRequest(receiveResult + "/" + data, token.get()), HttpResponse.BodyHandlers.ofString());
                if(applicationResponse.statusCode() == 202) {
                    model.addAttribute("message", applicationSubmitted + applicationResponse.body());
                } else model.addAttribute("message", applicationResponse.body());
            } else model.addAttribute("message", loginError);
        } catch (IOException | InterruptedException e) {
            model.addAttribute("message", sendError);
        } catch (URISyntaxException e) {
            model.addAttribute("message", urlError);
        } catch (ArrayIndexOutOfBoundsException e) {
            model.addAttribute("message", requestError);
        }
        return "template1";
    }

}
