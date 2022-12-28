package tt.authorization.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tt.authorization.model.User;

public interface UserRepository extends JpaRepository<User, String> {

    /**
     * This query checks if the email and password that user has entered into the form both contain in the database.
     * @param email An email of a user.
     * @param password A password of a user.
     * @return true if the credentials do contain in the database and false if they don't.
     */
    @Query(value = "SELECT CASE WHEN COUNT(email) > 0 THEN TRUE ELSE FALSE END FROM users WHERE email = :email AND password = :password",
            nativeQuery = true)
    boolean credentialsAreCorrect(@Param("email") String email, @Param("password") String password);

}