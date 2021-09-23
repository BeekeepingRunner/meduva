package com.szusta.meduva.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class EmailResetService {

    @Value("${meduva.app.front_base_url}")
    String baseURL;

}
