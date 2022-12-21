package tt.hashtranslator.util;

import com.google.gson.Gson;
import org.springframework.lang.Nullable;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;

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

    public static HttpRequest buildGetRequest(String URL, Map<String, String> requestParams) {
        try {
            String newURL = URL + "?" + String.join("&", (requestParams.keySet().stream()
                    .map(key -> key + "=" + requestParams.get(key)).toList()));
            return HttpRequest.newBuilder().uri(new URI(newURL))
                    .headers("Content-Type", "application/json").GET().build();
        } catch(URISyntaxException e) {
            return HttpRequest.newBuilder().build();
        }
    }

    public static Map<String, String> mapOfParams(String hash) {
        HashMap<String, String> params = new HashMap<>();
        params.put("hash", hash);
        params.put("hash_type", "md5");
        params.put("email", "bogimoogu@gmail.com");
        params.put("code", "9cab78bb777932a8");
        return params;
    }

}
