package com.example.workaccounting.infrastructure.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.ya")
public class YandexProperties {
    private String user;
    private String password;
    private String caldavUrl;
}
