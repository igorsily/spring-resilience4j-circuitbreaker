package com.br.igorsily.springresilience4jcircuitbreaker.model;

public record Activity(
        String activity,
        String type,
        double participants,
        double price,
        String link,
        String key,
        String accessibility
) {
}
