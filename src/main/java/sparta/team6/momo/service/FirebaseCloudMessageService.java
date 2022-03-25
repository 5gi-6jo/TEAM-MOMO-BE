package sparta.team6.momo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sparta.team6.momo.dto.FcmMessage;
import sparta.team6.momo.model.Plan;
import sparta.team6.momo.repository.PlanRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
@Slf4j
@RequiredArgsConstructor
@EnableScheduling
public class FirebaseCloudMessageService {
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/momo-cbc21/messages:send";
    //    private final String API_URL = "https://fcm.googleapis.com/v1/projects/test-pwa-b91b2/messages:send";
    private final ObjectMapper objectMapper;
    private final PlanRepository planRepository;

    public void sendMessageTo(String targetToken, String title, String body, String url) throws IOException {


        String message = makeMessage(targetToken, title, body, url);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();

        log.info(response.body().string());
    }

    @Scheduled(cron = "0 0/5 * * * *")
    @Transactional
    public void test() throws Exception {
        log.info(new Date() + "스케쥴러 실행");
//        LocalDateTime start = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
//        LocalDateTime end = start.plusMinutes(5);
        LocalDateTime start = LocalDateTime.now().minusMonths(1);
        LocalDateTime end = LocalDateTime.now().plusMonths(1);
        List<Plan> planList = planRepository.findAllByNoticeTimeBetween(start, end);

        for (Plan plan : planList) {
            sendMessageTo(plan.getAccount().getToken(), "테스트", "테스트", plan.getUrl());
        }
    }


    private String makeMessage(String targetToken, String title, String body, String url) throws JsonProcessingException {

//        List<String> tokenList = IntStream.rangeClosed(1, 20).mapToObj(index
//                -> push.getRegistrationToken()).collect(Collectors.toList());

        FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                        .token(targetToken)
                        .notification(FcmMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .data(FcmMessage.FcmData.builder()
                                        .url(url)
                                        .build()
                                )
                                .build()
                        )
                        .build()
                )
                .validateOnly(false)
                .build();

        log.info(objectMapper.writeValueAsString(fcmMessage));
        return objectMapper.writeValueAsString(fcmMessage);
    }

    // AccessToken 발급 받기
    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/firebase_service_key.json";

        GoogleCredentials googleCredential = GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath)
                .getInputStream()).createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredential.refreshIfExpired();
        return googleCredential.getAccessToken().getTokenValue();
    }
}
