package com.sparta.team6.momo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.sparta.team6.momo.dto.FcmMessage;
import com.sparta.team6.momo.dto.response.FcmResponseDto;
import com.sparta.team6.momo.exception.CustomException;
import com.sparta.team6.momo.exception.ErrorCode;
import com.sparta.team6.momo.model.Plan;
import com.sparta.team6.momo.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.sparta.team6.momo.exception.ErrorCode.PLAN_NOT_FOUND;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;


@Service
@Slf4j
@RequiredArgsConstructor
@EnableScheduling
public class FirebaseCloudMessageService {

    private static final String API_URL = "https://fcm.googleapis.com/v1/projects/momo-cbc21/messages:send";
    private final ObjectMapper objectMapper;
    private final PlanRepository planRepository;

    public void sendMessageTo(String targetToken, String title, String body) throws IOException {
        String message = makeMessage(targetToken, title, body);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));

        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(CONTENT_TYPE, "application/json; UTF-8")
                .build();
        Response response = client.newCall(request).execute();

        log.info(Objects.requireNonNull(response.body()).string());
    }


    @Transactional(readOnly = true)
    @Scheduled(cron = "0 0/5 * * * *")
    public void noticeScheduler() throws InterruptedException {
        log.info(new Date() + "???????????? ??????");
        LocalDateTime dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        List<Plan> planList = planRepository.findAllByNoticeTime(dateTime);
        log.info("DB ?????? ??????");

        ExecutorService executorService = Executors.newCachedThreadPool();

        // DB ?????? ????????? ???????????? ?????? ??????
        Thread.sleep(5000);
        for (Plan plan : planList) {
            Long lastMinutes = ChronoUnit.MINUTES.between(plan.getNoticeTime(), plan.getPlanDate());
            executorService.execute(task(plan.getUser().getToken(), lastMinutes));
        }
    }


    public Runnable task(String token, Long lastMinutes) {
        return () -> {
            try {
                String body = String.format("???????????? %d??? ????????????!", lastMinutes);
                sendMessageTo(token, "????????????(Momo)", body);
                log.info("push message ?????? ??????");
            } catch (IOException e) {
                e.printStackTrace();
                log.error("push message ?????? ??????");
            }
        };
    }


    private String makeMessage(String targetToken, String title, String body) throws JsonProcessingException {
        FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                        .token(targetToken)
                        .notification(FcmMessage.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        )
                        .build()
                )
                .validateOnly(false)
                .build();

        log.info(objectMapper.writeValueAsString(fcmMessage));
        return objectMapper.writeValueAsString(fcmMessage);
    }


    // AccessToken ?????? ??????
    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/firebase_service_key.json";

        GoogleCredentials googleCredential = GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath)
                .getInputStream()).createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredential.refreshIfExpired();
        log.info("FCM access token ?????? ??????");
        return googleCredential.getAccessToken().getTokenValue();
    }


    public FcmResponseDto manualPush(Long planId, Long userId) {
        Plan plan = planRepository.findById(planId).orElseThrow(
                () -> new CustomException(PLAN_NOT_FOUND)
        );
        if (userId.equals(plan.getUser().getId())) {
            return FcmResponseDto.of(plan);
        }
        log.info("Account ????????? ???????????? ????????????");
        throw new CustomException(ErrorCode.UNAUTHORIZED_MEMBER);
    }
}