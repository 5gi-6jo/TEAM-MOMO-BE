package sparta.team6.momo.utils.FCM;

import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

// AccessToken 발급 받기

public class FirebaseCloudMessageService {
    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/firebase_service_key.json";

        GoogleCredentials googleCredential = GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath)
                .getInputStream()).createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredential.refreshIfExpired();
        return googleCredential.getAccessToken().getTokenValue();
    }
}
