package tt.hashtranslator.util;

import org.springframework.lang.Nullable;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;

public class Utils {


    public static HttpRequest buildPostRequest(String URL, String requestBody, @Nullable String token) {
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(new URI(URL));
            if(token != null) {
                return requestBuilder.headers("Content-Type", "application/json", "Authorization", "Basic " + token)
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();
            }
            return requestBuilder.headers("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();
        } catch(URISyntaxException e) {
            return HttpRequest.newBuilder().build();
        }
    }

}
