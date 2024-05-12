package org.javaacademy.onlinebank.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class BankProperties {
    @Value("${integration.bank.name}")
    private String name;
    @Value("${integration.bank.partner.url}")
    private String partnerUrl;
    @Value("${server.port}")
    private Integer port;
}
