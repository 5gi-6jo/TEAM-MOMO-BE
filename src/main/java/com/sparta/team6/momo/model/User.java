package com.sparta.team6.momo.model;

import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.sparta.team6.momo.model.Provider.KAKAO;
import static com.sparta.team6.momo.model.UserRole.ROLE_USER;

@Entity
@Getter
@Validated
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@PrimaryKeyJoinColumn(name = "user_id")
public class User extends Account {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "isNoticeAllowed", nullable = false)
    private boolean isNoticeAllowed;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Plan> planList = new ArrayList<>();

    @Builder
    public User(@NonNull String email, @NonNull String password, @NonNull String nickname, @NonNull UserRole userRole, @NonNull Provider provider) {
        super(nickname, userRole);
        this.email = email;
        this.password = password;
        this.isNoticeAllowed = false;
        this.provider = provider;
    }

    public static User fromKakao(OAuth2User oAuth2User) {
        Map<String, String> kakao_account = oAuth2User.getAttribute("kakao_account");
        Map<String, String> properties = oAuth2User.getAttribute("properties");
        String email = String.valueOf(kakao_account.get("email"));
        String password = new BCryptPasswordEncoder().encode(UUID.randomUUID().toString());
        String nickname = properties.get("nickname");
        return User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .userRole(ROLE_USER)
                .provider(KAKAO)
                .build();
    }

    public void changeNoticeAllowed() {
        this.isNoticeAllowed = !this.isNoticeAllowed;
    }
}
