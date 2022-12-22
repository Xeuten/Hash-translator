package tt.hashtranslator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import tt.hashtranslator.model.DecryptApplication;
import tt.hashtranslator.model.DecryptApplicationStatus;
import tt.hashtranslator.model.DecryptRequest;
import tt.hashtranslator.persistence.DecryptApplicationRepo;
import tt.hashtranslator.util.Utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.HashMap;

@Service
public class ApplicationService {

    @Autowired
    private DecryptApplicationRepo decryptApplicationRepo;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${messages.incorrect_header}")
    private String incorrectHeader;

    @Value("${messages.validation_send_failed}")
    private String validationSendFailed;

    @Value("${messages.url_error}")
    private String urlError;

    @Value("${urls.validation}")
    private String validationUrl;

    @Value("${urls.decrypt_site}")
    private String decryptSite;

    @Value("${data.error_code}")
    private String code;

    /**
     * This is the central method of the translator service. Firstly, it checks if the request has the authorization
     * header. Secondly it interacts with the authorization service to validate the token that was passesd in the header.
     * Thirdly, it constructs an application object, saves it in the database and interacts with the website, that
     * decrypts the hashes. Finally, it receives the results of the decryption from the website and updates the
     * hashes' decodings and application's status. If there weren't any errors, the method returns a response with
     * status 200 and the id the application.
     * decodings and
     * @param decryptRequest The request is essentially the list of hashes to decrypt.
     * @param authHeader The header that is used to authenticate the user.
     * @return The response entity that contains status and a body.
     */
    public ResponseEntity<String> sendApplicationResponse(DecryptRequest decryptRequest, String authHeader) {
        String[] headerParts = authHeader.split(" ");
        if(!headerParts[0].equals("Basic") || headerParts.length != 2) {
            return ResponseEntity.status(400).body(incorrectHeader);
        }
        try {
            HttpResponse<String> headerValidationResponse = HttpClient.newHttpClient()
                    .send(Utils.buildGetRequest(validationUrl, headerParts[1]), HttpResponse.BodyHandlers.ofString());
            if(headerValidationResponse.statusCode() == 200) {
                DecryptApplication application = new DecryptApplication(decryptRequest);
                decryptApplicationRepo.save(application);
                String id = application.getId();
                HashMap<String, String> map = application.getMapHash();
                boolean allDecrypted = true;
                for(String hash : decryptRequest.hashes) {
                    HttpResponse<String> decryptResponse = HttpClient.newHttpClient().send(Utils
                            .buildGetRequestWithParams(decryptSite, Utils.mapOfParams(hash)), HttpResponse.BodyHandlers.ofString());
                    String body = decryptResponse.body();
                    if(decryptResponse.statusCode() != 200 || (body.length() == 16 && body.substring(0,13).equals(code))
                        || body.equals("")) allDecrypted = false;
                    else {
                        map.put(hash, body);
                        mongoTemplate.updateFirst(new Query(Criteria.where("id").is(id)),
                                new Update().set("mapHash", map), DecryptApplication.class);
                    }
                }
                mongoTemplate.updateFirst(new Query(Criteria.where("id").is(id)), new Update().set("status",
                        allDecrypted ? DecryptApplicationStatus.DECRYPTED : DecryptApplicationStatus.PROCESSED),
                        DecryptApplication.class);
                return ResponseEntity.status(202).body(id);
            }
            return ResponseEntity.status(400).body(headerValidationResponse.body());
        } catch(IOException | InterruptedException e) {
            return ResponseEntity.status(400).body(validationSendFailed);
        } catch(URISyntaxException e) {
            return ResponseEntity.status(400).body(urlError);
        }
    }

}
