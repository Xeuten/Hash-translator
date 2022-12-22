package tt.hashtranslator.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;
import tt.hashtranslator.model.DecryptApplication;

public interface DecryptApplicationRepo extends MongoRepository<DecryptApplication, String> {}
