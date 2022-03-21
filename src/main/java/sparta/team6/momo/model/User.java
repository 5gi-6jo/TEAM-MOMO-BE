package sparta.team6.momo.model;

import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;
import sparta.team6.momo.dto.SignupRequestDto;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.*;

import static sparta.team6.momo.model.UserRole.ROLE_USER;

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
    private String password;

    @Column(nullable = false)
    @NotEmpty
    private String nickname;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Plan> planList = new ArrayList<>();


    public User(@NonNull String email, @NonNull String password, @NonNull String nickname, @NonNull UserRole userRole) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.userRole = userRole;
    }

    public static User fromKakao(OAuth2User oAuth2User) {
        Map<String, Object> kakao_account = oAuth2User.getAttribute("kakao_account");
        Map<String, String> properties = oAuth2User.getAttribute("properties");
        String email = String.valueOf(kakao_account.get("email"));
        String password = new BCryptPasswordEncoder().encode(UUID.randomUUID().toString());
        String nickname = properties.get("nickname");
        return new User(email, password, nickname, ROLE_USER);
    }
}
