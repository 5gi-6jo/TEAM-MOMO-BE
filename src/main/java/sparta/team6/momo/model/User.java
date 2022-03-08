package sparta.team6.momo.model;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter
@Validated
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    @NotEmpty
    private String email;

    @Column(nullable = false)
    @NotEmpty
    private String nickname;

    @Column(nullable = false)
    @NotEmpty
    private String password;
}
