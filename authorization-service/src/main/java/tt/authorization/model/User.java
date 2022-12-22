package tt.authorization.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class is used to perform ORM of users. A user has an email and a password.
 */
@Data
@Entity
@Table(name="users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column
    public String email;

    @Column
    public String password;

}
