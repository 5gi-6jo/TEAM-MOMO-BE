package com.sparta.team6.momo.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@Validated
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
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

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Plan> planList = new ArrayList<>();

    @Column
    private String token;

    public Account(@NonNull String email, @NonNull String password, @NonNull String nickname, @NonNull UserRole userRole) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.userRole = userRole;
    }

    private static final String prefix = "GUEST_";
    private static final String suffix = "@MOMO.COM";
    public static Account createGuest(String nickname) {
        String pw = UUID.randomUUID().toString();
        String email = prefix + nickname + suffix;
        return new Account(email, pw, nickname, UserRole.ROLE_GUEST);
    }


    public static Account fromKakao(OAuth2User oAuth2User) {
        Map<String, String> kakao_account = oAuth2User.getAttribute("kakao_account");
        Map<String, String> properties = oAuth2User.getAttribute("properties");
        String email = String.valueOf(kakao_account.get("email"));
        String password = new BCryptPasswordEncoder().encode(UUID.randomUUID().toString());
        String nickname = properties.get("nickname");
        return new Account(email, password, nickname, UserRole.ROLE_USER);
    }

    public void updateToken(String token) {
        this.token = token;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}
