package ru.verstache.gabella.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ServerDto(UUID id, String name, LocalDateTime createdAt) {
}
