package tt.hashtranslator.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.UUID;

/**
 * This class is used to perform ORM of the decryption application. An application has a unique id (in UUID format), a
 * hash map of hashes and their decodings and a status.
 */
@Data
@NoArgsConstructor
@Document(collection = "applications")
public class DecryptApplication {

    public DecryptApplication(DecryptRequest request) {
        this.id = UUID.randomUUID().toString();
        this.status = DecryptApplicationStatus.PROCESSING.toString();
        HashMap<String, String> map = new HashMap<>();
        request.hashes.forEach(hash -> map.put(hash, ""));
        this.mapHash = map;
    }

    @Id
    private String id;

    private String status;

    private HashMap<String, String> mapHash;

    public void setStatus(DecryptApplicationStatus status) { this.status = status.toString(); }

}
