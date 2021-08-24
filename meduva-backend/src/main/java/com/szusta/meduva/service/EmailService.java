package com.szusta.meduva.service;

import com.szusta.meduva.model.email.AbstractEmailContext;
import org.springframework.stereotype.Component;

@Component
public interface EmailService {

    void sendMail(final AbstractEmailContext email) throws Exception;
}
