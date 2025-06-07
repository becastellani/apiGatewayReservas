package com.reservaservice.demo.infrastructure.messaging.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UserEventDto {
    private Long id;
    private NomeDto nome;


    @Data
    @NoArgsConstructor
    public static class NomeDto {
        private String nome;
    }
}