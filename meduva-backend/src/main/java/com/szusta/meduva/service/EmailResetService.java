package com.szusta.meduva.service;

import com.szusta.meduva.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class EmailResetService {

    privay

    @Value("${meduva.app.front_base_url}")
    String baseURL;

    EmailResetService(){

    }

    public void deletePreviousResetTokens(User user) {

    }
}
