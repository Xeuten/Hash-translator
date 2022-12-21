package tt.hashtranslator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import tt.hashtranslator.model.DecryptApplication;
import tt.hashtranslator.model.DecryptApplicationStatus;
import tt.hashtranslator.model.DecryptRequest;
import tt.hashtranslator.persistence.DecryptApplicationRepository;
import tt.hashtranslator.util.Utils;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.UUID;

@Slf4j
@Service
public class ApplicationService {

    @Autowired
    private DecryptApplicationRepository decryptApplicationRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${messages.absent_auth}")
    private String absentAuth;

    @Value("${messages.incorrect_header}")
    private String incorrectHeader;

    @Value("${messages.validation_send_failed}")
    private String validationSendFailed;

    @Value("${urls.validation}")
    private String validationUrl;

    @Value("${urls.decrypt_site}")
    private String decryptSite;

    @Value("${data.error_code}")
    private String code;

    public ResponseEntity<String> sendApplicationResponse(DecryptRequest request, @Nullable String authHeader) {
        if(authHeader == null) {
            return ResponseEntity.status(400).body(absentAuth);
        }
        String[] headerParts = authHeader.split(" ");
        if(!headerParts[0].equals("Basic") || headerParts.length != 2) {
            return ResponseEntity.status(400).body(incorrectHeader);
        }
        try {
            HttpResponse<String> headerValidationResponse = HttpClient.newHttpClient()
                    .send(Utils.buildPostRequest(validationUrl, headerParts[1], null), HttpResponse.BodyHandlers.ofString());
            if(headerValidationResponse.statusCode() == 200) {
                DecryptApplication application = new DecryptApplication(request);
                decryptApplicationRepository.save(application);
                String id = application.getId();
                HashMap<String, String> map = application.getMapHash();
                boolean allDecrypted = true;
                for(String hash : request.hashes) {
                    HttpResponse<String> decryptResponse = HttpClient.newHttpClient().send(Utils
                            .buildGetRequest(decryptSite, Utils.mapOfParams(hash)), HttpResponse.BodyHandlers.ofString());
                    String body = decryptResponse.body();
                    if(decryptResponse.statusCode() != 200 || (body.length() == 16 && body.substring(0,13).equals(code))) {
                        allDecrypted = false;
                    } else {
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
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(400).body(validationSendFailed);
        }
    }

}
