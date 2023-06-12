package com.Udemy.YeoGiDa.global.fcm.service;

import com.Udemy.YeoGiDa.global.fcm.dto.FcmMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FirebaseCloudMessageService {

    //YML
    private String API_URL = "https://fcm.googleapis.com/v1/projects/yeogida-552d2/messages:send";
    private final ObjectMapper objectMapper;

    public void sendMessageTo(String targetToken, String title, String body, String alarmType, String targetId) throws IOException {
        String message = makeMessage(targetToken, title, body, alarmType, targetId);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message, MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        //에러 처리
        Response response = client.newCall(request)
                .execute();

        System.out.println(response.body().string());
    }

    private String makeMessage(String targetToken, String title, String body, String alarmType, String targetId) throws JsonProcessingException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("title", title);
        data.put("body", body);
        data.put("alarmType", alarmType);
        data.put("targetId", targetId);
        FcmMessage fcmMessage = FcmMessage.builder()
                .message(FcmMessage.Message.builder()
                        .token(targetToken)
                        .data(data)
                        .build()
                )
                .validate_only(false)
                .build();

        return objectMapper.writeValueAsString(fcmMessage);
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "/firebase/yeogida_fcm_api_key.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
