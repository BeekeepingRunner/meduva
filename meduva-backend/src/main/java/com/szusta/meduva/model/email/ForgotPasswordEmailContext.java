package com.szusta.meduva.model.email;

import com.szusta.meduva.model.User;
import org.springframework.web.util.UriComponentsBuilder;

public class ForgotPasswordEmailContext extends AbstractEmailContext {

    private String token;

    @Override
    public <T> void init(T context) {
        User user = (User) context;
        put("firstName", user.getName());
        setTemplateLocation("emails/forgot-password");
        setSubject("Forgotten Password");
        setFrom("bartosz.biegajlo@pollub.edu.pl");
        setTo(user.getEmail());
    }

    public void setToken(String token) {
        this.token = token;
        put("token", token);
    }

    public void buildVerificationUrl(final String baseURL, final String token) {

        final String url = UriComponentsBuilder.fromHttpUrl(baseURL)
                .path("/login/reset-password").queryParam("token", token).toUriString();

        put("verificationURL", url);
    }
}
