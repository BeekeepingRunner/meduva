package com.szusta.meduva.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MeduvaConstants {

    @Value("${meduva.app.base_url}")
    public static String APP_BASE_URL;
}
