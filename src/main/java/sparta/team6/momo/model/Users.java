package sparta.team6.momo.model;

<<<<<<< HEAD
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Users extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;
=======
public class Users {
>>>>>>> 5f2ef452226787359a59b1868a94c9bea69d6716
}
