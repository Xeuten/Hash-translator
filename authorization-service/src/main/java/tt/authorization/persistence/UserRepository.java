package tt.authorization.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tt.authorization.model.User;

public interface UserRepository extends JpaRepository<User, String> {

    @Query(value = "SELECT CASE WHEN COUNT(email) > 0 THEN TRUE ELSE FALSE END FROM users WHERE email = :email AND password = :password",
            nativeQuery = true)
    boolean credentialsAreCorrect(@Param("email") String email, @Param("password") String password);

}
