package org.javaacademy.onlinebank.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.javaacademy.onlinebank.config.BankProperties;
import org.javaacademy.onlinebank.dto.TransferDto;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;

@Service
@Data
@RequiredArgsConstructor
public class TransferService {
    private static final String postFixUrl = "/operations/receive";
    private final BankProperties properties;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void transferToAnotherBank(TransferDto transferDto) {
        RequestEntity
                .post(properties.getPartnerUrl() + postFixUrl)
                .accept(MediaType.APPLICATION_JSON)
                .body(transferDto);
    }
}
