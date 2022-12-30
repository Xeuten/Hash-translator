package tt.hashtranslator.error;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

@ControllerAdvice
public class ErrorHandler {

    @Value("${messages.incorrect_json}")
    private String incorrectJson;

    @Value("${messages.login_error}")
    private String loginError;

    @Value("${messages.send_error}")
    private String sendError;

    @Value("${messages.validation_send_failed}")
    private String validationSendFailed;

    @Value("${messages.url_error}")
    private String urlError;

    /**
     * This handler is invoked when conversion of the user's input into DecryptRequest object has failed.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleMessageNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(incorrectJson);
    }

    /**
     * This handler is invoked when either the user tries to access the URLs that are mapped to POST requests with a
     * GET request, or the user tries to access the URLs that require an authorization header.
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class, MissingRequestHeaderException.class})
    public ResponseEntity<String> handleMethodNotSupportedAndMissingHeader(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(loginError);
    }

    /**
     * This handler is invoked when the sending of a request fails. This situation could occur in different methods,
     * so the object that is returned by the method is dependent of the method which threw the exception.
     * @param ex The exception that was thrown. By analysing its stack trace we can find out, which method threw the
     * exception.
     * @param model The model that will be passed to the view in case that the exception was thrown in
     * "transitionalResponse" method.
     * @return If the exception was thrown in "transitionalResponse" method, the method returns a string that is
     * transformed into a HTML document by Thymeleaf engine. Otherwise, the method returns a response entity with
     * "BAD REQUEST" status and a corresponding error message.
     */
    @ExceptionHandler({IOException.class, InterruptedException.class})
    public Object handleIOEAndInterrupted(Exception ex, Model model) {
        if(Arrays.stream(ex.getStackTrace())
                .anyMatch(element -> element.getMethodName().equals("transitionalResponse"))) {
            model.addAttribute("message", sendError);
            return "template1";
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validationSendFailed);
    }

    /**
     * This handler is invoked when the string that was interpreted as the URL to send a request turned out to have
     * incorrect format. This situation could occur in different methods, so the object that is returned by the method
     * is dependent of the method which threw the exception.
     * @param ex The exception that was thrown. By analysing its stack trace we can find out, which method threw the
     * exception.
     * @param model The model that will be passed to the view in case that the exception was thrown in
     * "transitionalResponse" method.
     * @return If the exception was thrown in "transitionalResponse" method, the method returns a string that is
     * transformed into a HTML document by Thymeleaf engine. Otherwise, the method returns a response entity with
     * "BAD REQUEST" status and a corresponding error message.
     */
    @ExceptionHandler(URISyntaxException.class)
    public Object handleSyntax(URISyntaxException ex, Model model) {
        if(Arrays.stream(ex.getStackTrace())
                .anyMatch(element -> element.getMethodName().equals("transitionalResponse"))) {
            model.addAttribute("message", sendError);
            return "template1";
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(urlError);
    }
}
